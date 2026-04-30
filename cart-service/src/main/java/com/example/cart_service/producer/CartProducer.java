package com.example.cart_service.producer;

import com.example.cart_service.dto.CartEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CartProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CartProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(CartEvent event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("cart-topic", json);
            System.out.println("Sent Event: " + json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}