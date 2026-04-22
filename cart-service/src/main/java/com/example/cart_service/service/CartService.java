package com.example.cart_service.service;

import com.example.cart_service.entity.Cart;
import com.example.cart_service.entity.CartItem;
import com.example.cart_service.repository.CartItemRepository;
import com.example.cart_service.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    // Create cart
    public Cart createCart(Cart cart) {
        return cartRepository.save(cart);
    }

    // Add item to cart
    public CartItem addItemToCart(CartItem item) {
        return cartItemRepository.save(item);
    }

    // Get all carts
    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    // Get all items
    public List<CartItem> getAllItems() {
        return cartItemRepository.findAll();
    }
}