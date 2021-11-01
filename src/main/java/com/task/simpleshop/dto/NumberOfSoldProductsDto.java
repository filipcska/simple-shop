package com.task.simpleshop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public  record NumberOfSoldProductsDto(@JsonProperty("number_of_sold_products") Long soldProducts){
}
