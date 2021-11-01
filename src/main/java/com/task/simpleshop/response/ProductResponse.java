package com.task.simpleshop.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.task.simpleshop.db.entity.Product;

import java.time.LocalDateTime;

public record ProductResponse(@JsonProperty("product_id") long id,
                              @JsonProperty("product_name") String name,
                              @JsonProperty("product_creation_date") @JsonFormat(pattern = "dd-MM-yyyy") LocalDateTime createdAt,
                              @JsonProperty("product_status") String status) {


    public ProductResponse(Product product) {
        this(product.getId(), product.getName(), product.getCreatedAt(), product.getStatus().getValue());
    }
}