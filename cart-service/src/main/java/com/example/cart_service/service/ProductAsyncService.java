package com.example.cart_service.service;

import com.example.cart_service.dto.ProductDto;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CompletableFuture;

@Service
public class ProductAsyncService {

    private final WebClient webClient;

    public ProductAsyncService(WebClient webClient) {
        this.webClient = webClient;
    }

    @Async
    public CompletableFuture<ProductDto> getProduct(Integer productId) {

        ProductDto product = webClient.get()
                .uri("http://localhost:8081/products/" + productId)
                .retrieve()
                .bodyToMono(ProductDto.class)
                .block();

        return CompletableFuture.completedFuture(product);
    }
}