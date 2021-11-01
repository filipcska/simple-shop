package com.task.simpleshop.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductPopularityRequest(@JsonProperty("number_of_products_to_display") Integer numberOfProductsToDisplay) {
}
