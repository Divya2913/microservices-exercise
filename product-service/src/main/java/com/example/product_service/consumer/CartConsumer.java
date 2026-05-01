package com.example.product_service.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CartConsumer {

    @KafkaListener(topics = "cart-topic", groupId = "product-group")
    public void consume(String message) {
        System.out.println("Received Kafka Event: " + message);
    }
}