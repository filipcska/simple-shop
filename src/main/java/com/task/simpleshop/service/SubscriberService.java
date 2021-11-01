package com.task.simpleshop.service;

import com.task.simpleshop.db.entity.Product;
import com.task.simpleshop.db.entity.Subscriber;
import com.task.simpleshop.db.repository.ProductRepository;
import com.task.simpleshop.db.repository.SubscriberRepository;
import com.task.simpleshop.dto.PurchasedProductDto;
import com.task.simpleshop.exception.SubscriberNotFoundException;
import com.task.simpleshop.request.SubscriberRequest;
import com.task.simpleshop.response.ProductResponse;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriberService {


    private SubscriberRepository subscriberRepository;

    private ProductRepository productRepository;

    @Autowired
    public SubscriberService(SubscriberRepository subscriberRepository, ProductRepository productRepository) {
        this.subscriberRepository = subscriberRepository;
        this.productRepository = productRepository;
    }

    public Subscriber addSubscriber(SubscriberRequest request) {
        Subscriber subscriber = new Subscriber();
        ModelMapper mapper = new ModelMapper();

        mapper.map(request, subscriber);
        return subscriberRepository.save(subscriber);
    }

    public Subscriber getSubscriber(long subscriberId) throws SubscriberNotFoundException {
        return subscriberRepository.findById(subscriberId).orElseThrow(() ->
                new SubscriberNotFoundException(String.format("Subscriber with id %s is not found", subscriberId)));
    }

    public List<ProductResponse> getAllProductsBySubscriber(long subscriberId) throws SubscriberNotFoundException {
        final Subscriber subscriber = subscriberRepository.findById(subscriberId).orElseThrow(() ->
                new SubscriberNotFoundException(String.format("Subscriber with id %s is not found", subscriberId)));

        final List<Product> productList = subscriber.getProducts();

        return productList.stream().
                map(ProductResponse::new).toList();
    }

    public Subscriber updateSubscriber(SubscriberRequest subscriberRequest, long subscriberId) throws SubscriberNotFoundException {
        final Subscriber subscriber = subscriberRepository.findById(subscriberId).orElseThrow(() ->
                new SubscriberNotFoundException(String.format("Subscriber with id %s is not found", subscriberId)));

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        modelMapper.map(subscriberRequest, subscriber);

        return subscriberRepository.save(subscriber);
    }


    public void deleteSubscriber(long subscriberId) throws SubscriberNotFoundException {
        final Subscriber subscriber = subscriberRepository.findById(subscriberId).orElseThrow(() ->
                new SubscriberNotFoundException(String.format("Subscriber with id %s is not found", subscriberId)));
        subscriberRepository.delete(subscriber);

    }


    public List<Subscriber> getAllSubscribers() {
        return subscriberRepository.findAll();
    }

    public PurchasedProductDto purchaseProduct(long subscriberId, List<Long> productIds) throws SubscriberNotFoundException {
        final Subscriber subscriber = subscriberRepository.findById(subscriberId).orElseThrow(() ->
                new SubscriberNotFoundException(String.format("Subscriber with id %s is not found", subscriberId)));
        final List<Product> purchasedProducts = productRepository.findAllById(productIds);

        subscriber.getProducts().addAll(purchasedProducts);
        subscriberRepository.save(subscriber);

        final List<String> purchasedProductsNames = purchasedProducts.stream().map(Product::getName).toList();
        return new PurchasedProductDto(purchasedProductsNames);

    }
}
