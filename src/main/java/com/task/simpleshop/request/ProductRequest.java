package com.task.simpleshop.request;

import com.task.simpleshop.status.ProductStatus;

public class ProductRequest {

    private String name;

    private ProductStatus status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }
}
