package com.task.simpleshop.status;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ProductStatus {

    AVAILABLE ("Available"),
    ON_SALE ("On sale"),
    NOT_AVAILABLE ("Not Available");

    private String value;

    ProductStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return this.value;
    }
}
