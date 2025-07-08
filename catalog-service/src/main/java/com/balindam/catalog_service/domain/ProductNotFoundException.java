package com.balindam.catalog_service.domain;

public class ProductNotFoundException extends RuntimeException {
    ProductNotFoundException(String message) {
        super(message);
    }

    public static ProductNotFoundException forCode(String code) {
        return new ProductNotFoundException("Product with code: " + code + " not found");
    }
}
