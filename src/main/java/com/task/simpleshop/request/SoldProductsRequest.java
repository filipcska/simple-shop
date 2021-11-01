package com.task.simpleshop.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.task.simpleshop.status.ProductStatus;

public record SoldProductsRequest(@JsonProperty("start_date") String startDate,
                                  @JsonProperty("end_date") String endDate,
                                  ProductStatus status) {
}
