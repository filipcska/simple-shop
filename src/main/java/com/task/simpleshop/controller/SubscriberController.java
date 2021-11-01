package com.task.simpleshop.controller;


import com.task.simpleshop.db.entity.Subscriber;
import com.task.simpleshop.dto.PurchasedProductDto;
import com.task.simpleshop.exception.SubscriberNotFoundException;
import com.task.simpleshop.request.PurchaseRequest;
import com.task.simpleshop.request.SubscriberRequest;
import com.task.simpleshop.response.ProductResponse;
import com.task.simpleshop.response.SubscriberResponse;
import com.task.simpleshop.service.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(
        value = "/subscribers"
)

public class SubscriberController {

    private final String SUBSCRIBER_NOT_FOUND = "Subscriber with id %s is not found";

    private SubscriberService subscriberService;

    @Autowired
    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping
    public ResponseEntity<SubscriberResponse> addSubscriber(@RequestBody SubscriberRequest subscriberRequest) {
        final Subscriber subscriber = subscriberService.addSubscriber(subscriberRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SubscriberResponse(subscriber));
    }

    @GetMapping("/{subscriberId}")
    public ResponseEntity<Object> getSubscriber(@PathVariable int subscriberId) throws SubscriberNotFoundException {
        final Subscriber subscriber = subscriberService.getSubscriber(subscriberId);
        return ResponseEntity.status(HttpStatus.OK).body(new SubscriberResponse(subscriber));
    }

    @PutMapping("/{subscriberId}")
    public ResponseEntity<Object> updateSubscriber(@PathVariable int subscriberId, @RequestBody SubscriberRequest subscriberRequest) throws SubscriberNotFoundException {

        final Subscriber subscriber = subscriberService.updateSubscriber(subscriberRequest, subscriberId);
        return ResponseEntity.status(HttpStatus.OK).body(new SubscriberResponse(subscriber));
    }

    @DeleteMapping("/{subscriberId}")
    public ResponseEntity<Object> deleteSubscriber(@PathVariable int subscriberId) throws SubscriberNotFoundException {
        subscriberService.deleteSubscriber(subscriberId);
        return new ResponseEntity<>(HttpStatus.OK.getReasonPhrase(), HttpStatus.OK);


    }

    @GetMapping("/products/{subscriberId}")
    public ResponseEntity<Object> getAcquiredProducts(@PathVariable int subscriberId) throws SubscriberNotFoundException {
        final List<ProductResponse> products = subscriberService.getAllProductsBySubscriber(subscriberId);
        if (products == null) {
            return new ResponseEntity<>(String.format(SUBSCRIBER_NOT_FOUND, subscriberId), HttpStatus.NOT_FOUND);
        } else if (products.isEmpty()) {
            return new ResponseEntity<>(String.format("Subscriber with id %s has not aquired any products", subscriberId), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @PutMapping("/purchase/{subscriberId}")
    public ResponseEntity<Object> buyProducts(@PathVariable long subscriberId, @RequestBody PurchaseRequest request) throws SubscriberNotFoundException {
        final PurchasedProductDto purchasedProductDto = subscriberService.purchaseProduct(subscriberId, request.productIds());
        return ResponseEntity.status(HttpStatus.OK).body(purchasedProductDto);

    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllSubribers() {
        return ResponseEntity.status(HttpStatus.OK).body(subscriberService.getAllSubscribers());
    }

}
