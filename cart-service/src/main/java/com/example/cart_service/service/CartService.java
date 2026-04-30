package com.example.cart_service.service;

import com.example.cart_service.dto.CartEvent;
import com.example.cart_service.dto.ProductDto;
import com.example.cart_service.entity.Cart;
import com.example.cart_service.entity.CartItem;
import com.example.cart_service.producer.CartProducer;
import com.example.cart_service.repository.CartItemRepository;
import com.example.cart_service.repository.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final WebClient webClient;
    private final CartProducer cartProducer;
    private final ProductAsyncService productAsyncService;

    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       WebClient webClient,
                       CartProducer cartProducer,
                       ProductAsyncService productAsyncService) {

        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.webClient = webClient;
        this.cartProducer = cartProducer;
        this.productAsyncService = productAsyncService;
    }

    // CREATE CART
    public Cart createCart(Cart cart) {
        return cartRepository.save(cart);
    }

    // GET ALL CARTS
    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    // GET ALL ITEMS
    public List<CartItem> getAllItems() {
        return cartItemRepository.findAll();
    }
    private static final Logger log =
            LoggerFactory.getLogger(CartService.class);

    // ADD ITEM TO CART WITH ASYNC PRODUCT FETCH + VALIDATION + KAFKA
    public CartItem addItemToCart(CartItem item) {

        log.info("Adding item to cart: {}", item);

        CompletableFuture<ProductDto> future =
                productAsyncService.getProduct(item.getProductId());

        ProductDto product = future.join();

        log.info("Product fetched successfully: {}", product);

        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        if (product.getStock() < item.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        CartItem savedItem = cartItemRepository.save(item);

        log.info("Cart item saved successfully");

        try {
            cartProducer.sendEvent(
                    new CartEvent(
                            item.getCartId(),
                            item.getProductId(),
                            item.getQuantity()
                    )
            );

            log.info("Kafka event sent successfully");

        } catch (Exception e) {

            log.error("Kafka publish failed", e);
        }

        return savedItem;
    }
}