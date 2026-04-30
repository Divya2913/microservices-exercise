package com.example.cart_service.controller;

import com.example.cart_service.entity.Cart;
import com.example.cart_service.entity.CartItem;
import com.example.cart_service.service.CartService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // CREATE CART
    @PostMapping
    public Cart createCart(@RequestBody Cart cart) {
        return cartService.createCart(cart);
    }

    // ADD ITEM
    @PostMapping("/item")
    public CartItem addItem(@Valid @RequestBody CartItem item) {
        return cartService.addItemToCart(item);
    }

    // GET CARTS
    @GetMapping
    public List<Cart> getCarts() {
        return cartService.getAllCarts();
    }

    // GET ITEMS
    @GetMapping("/items")
    public List<CartItem> getItems() {
        return cartService.getAllItems();
    }
}