package com.task.simpleshop.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PurchaseRequest(@JsonProperty("product_ids") List<Long> productIds) {
//
//    @JsonProperty("product_ids")
//    private List<Long> productIds;
//
//    public List<Long> getProductIds() {
//        return productIds;
//    }
//
//    public void setProductIds(List<Long> productIds) {
//        this.productIds = productIds;
//    }
}
