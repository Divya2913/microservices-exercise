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

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final WebClient webClient;
    private final CartProducer cartProducer;   // ✅ ADD HERE

    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       WebClient webClient,
                       CartProducer cartProducer) {   // ✅ ADD HERE

        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.webClient = webClient;
        this.cartProducer = cartProducer;      // ✅ ADD HERE
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

    // ADD ITEM TO CART WITH PRODUCT VALIDATION + KAFKA
    public CartItem addItemToCart(CartItem item) {

        ProductDto product = webClient.get()
                .uri("http://localhost:8081/products/" + item.getProductId())
                .retrieve()
                .bodyToMono(ProductDto.class)
                .block();

        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        if (product.getStock() < item.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        // Save item first
        CartItem savedItem = cartItemRepository.save(item);

        // ✅ Send Kafka Event
        try {
            cartProducer.sendEvent(
                    new CartEvent(
                            item.getCartId(),
                            item.getProductId(),
                            item.getQuantity()
                    )
            );
        }
        catch(Exception e) {
            e.printStackTrace();
        }


        return savedItem;
    }
}