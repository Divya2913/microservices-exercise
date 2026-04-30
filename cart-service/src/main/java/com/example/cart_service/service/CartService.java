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

    // ADD ITEM TO CART WITH ASYNC PRODUCT FETCH + VALIDATION + KAFKA
    public CartItem addItemToCart(CartItem item) {

        // Async call to Product Service
        CompletableFuture<ProductDto> future =
                productAsyncService.getProduct(item.getProductId());

        // Wait for result when needed
        ProductDto product = future.join();

        // Validate product exists
        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        // Validate stock
        if (product.getStock() < item.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        // Save cart item
        CartItem savedItem = cartItemRepository.save(item);

        // Publish Kafka event
        try {
            cartProducer.sendEvent(
                    new CartEvent(
                            item.getCartId(),
                            item.getProductId(),
                            item.getQuantity()
                    )
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return savedItem;
    }
}