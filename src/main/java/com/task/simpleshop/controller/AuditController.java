package com.task.simpleshop.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.task.simpleshop.dto.NumberOfSoldProductsDto;
import com.task.simpleshop.dto.ProductPopularityDto;
import com.task.simpleshop.request.ProductPopularityRequest;
import com.task.simpleshop.request.SoldProductsRequest;
import com.task.simpleshop.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping(
        value = "/audit"
)
public class AuditController {

    private AuditService auditService;

    @Autowired
    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping("/total-subscribers")
    public ResponseEntity<Object> getNumberOfSubscribers() {
        final Long numberOfSubscribers = auditService.getTotalNumberOfSubscribers();
        record SubscriberCountResponse(@JsonProperty("total_number_of_subscribers") Long subscribers){}
        return ResponseEntity.status(HttpStatus.OK).body(new SubscriberCountResponse(numberOfSubscribers));
    }


    @PostMapping("/most-popular-products")
    public ResponseEntity<Object> findMostPopularProducts(@RequestBody ProductPopularityRequest request) {
        final List<ProductPopularityDto> mostPopularProducts = auditService.getMostPopularProducts(request.numberOfProductsToDisplay() != null ? request.numberOfProductsToDisplay() : 1);
        return ResponseEntity.status(HttpStatus.OK).body(mostPopularProducts);
    }

    @PostMapping("/sold-products")
    public ResponseEntity<Object> getNumberOfSoldProducts(@RequestBody SoldProductsRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateTime startDate = LocalDate.parse(request.startDate(), formatter).atTime(LocalTime.MIN);
        LocalDateTime endDate = LocalDate.parse(request.endDate(), formatter).atTime(LocalTime.MAX);

        final NumberOfSoldProductsDto numberOfSoldProducts = auditService.getNumberOfSoldProducts(startDate, endDate, request.status());
        return  ResponseEntity.status(HttpStatus.OK).body(numberOfSoldProducts);
    }

}
