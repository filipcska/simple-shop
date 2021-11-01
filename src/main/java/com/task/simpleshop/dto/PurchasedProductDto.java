package com.task.simpleshop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PurchasedProductDto(@JsonProperty("successfully_purchased_products") List<String> productName) {
}
