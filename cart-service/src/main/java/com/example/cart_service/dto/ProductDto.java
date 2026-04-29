package com.example.cart_service.dto;

import lombok.Data;

@Data
public class ProductDto {

    private Integer id;
    private String name;
    private Double price;
    private Integer stock;
}