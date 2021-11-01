package com.task.simpleshop.service;

import com.task.simpleshop.db.entity.Product;
import com.task.simpleshop.db.entity.Subscriber;
import com.task.simpleshop.db.repository.ProductRepository;
import com.task.simpleshop.db.repository.SubscriberRepository;
import com.task.simpleshop.dto.NumberOfSoldProductsDto;
import com.task.simpleshop.dto.ProductPopularityDto;
import com.task.simpleshop.exception.SubscriberNotFoundException;
import com.task.simpleshop.request.ProductRequest;
import com.task.simpleshop.request.SubscriberRequest;
import com.task.simpleshop.status.ProductStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles({"test"})
@ExtendWith(SpringExtension.class)
class AuditServiceTest {

    @Autowired
    private AuditService auditService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SubscriberService subscriberService;

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private ProductRepository productRepository;

    private Product product1;
    private Product product2;
    private Product product3;
    private Product product4;

    @BeforeEach
    void setUp() throws SubscriberNotFoundException {
        final ProductRequest productRequest1 = new ProductRequest();
        productRequest1.setName("Chair");
        productRequest1.setStatus(ProductStatus.AVAILABLE);

        final ProductRequest productRequest2 = new ProductRequest();
        productRequest2.setName("Table");
        productRequest2.setStatus(ProductStatus.AVAILABLE);

        final ProductRequest productRequest3 = new ProductRequest();
        productRequest3.setName("Heater");
        productRequest3.setStatus(ProductStatus.AVAILABLE);

        final ProductRequest productRequest4 = new ProductRequest();
        productRequest4.setName("Cooler");
        productRequest4.setStatus(ProductStatus.ON_SALE);

        productService.addProduct(productRequest1);
        productService.addProduct(productRequest2);
        productService.addProduct(productRequest3);
        productService.addProduct(productRequest4);

        SubscriberRequest subscriberRequest1 = new SubscriberRequest();
        subscriberRequest1.setFirstName("Jack");
        subscriberRequest1.setLastName("Smith");

        SubscriberRequest subscriberRequest2 = new SubscriberRequest();
        subscriberRequest2.setFirstName("John");
        subscriberRequest2.setLastName("Cook");

        SubscriberRequest subscriberRequest3 = new SubscriberRequest();
        subscriberRequest3.setFirstName("Jake");
        subscriberRequest3.setLastName("DAVIS");

        SubscriberRequest subscriberRequest4 = new SubscriberRequest();
        subscriberRequest4.setFirstName("Amber");
        subscriberRequest4.setLastName("Heard");

        subscriberService.addSubscriber(subscriberRequest1);
        subscriberService.addSubscriber(subscriberRequest2);
        subscriberService.addSubscriber(subscriberRequest3);
        subscriberService.addSubscriber(subscriberRequest4);

        final List<Subscriber> subscriberList = subscriberRepository.findAll();
        final Subscriber subscriber1 = subscriberList.get(0);
        final Subscriber subscriber2 = subscriberList.get(1);
        final Subscriber subscriber3 = subscriberList.get(2);
        final Subscriber subscriber4 = subscriberList.get(3);

        final List<Product> productList = productRepository.findAll();
        product1 = productList.get(0);
        product2 = productList.get(1);
        product3 = productList.get(2);
        product4 = productList.get(3);

        final List<Product> productsForSubscriber1 = List.of(product1, product2, product3, product4);
        final List<Product> productsForSubscriber2 = List.of(product1, product2, product3);
        final List<Product> productsForSubscriber3 = List.of(product1, product2);
        final List<Product> productsForSubscriber4 = List.of(product1);

        subscriberService.purchaseProduct(subscriber1.getId(), productsForSubscriber1.stream().map(Product::getId).toList());
        subscriberService.purchaseProduct(subscriber2.getId(), productsForSubscriber2.stream().map(Product::getId).toList());
        subscriberService.purchaseProduct(subscriber3.getId(), productsForSubscriber3.stream().map(Product::getId).toList());
        subscriberService.purchaseProduct(subscriber4.getId(), productsForSubscriber4.stream().map(Product::getId).toList());


    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
        subscriberRepository.deleteAll();
    }

    @Test
    void getNumberOfSoldProductsTest() {
        final LocalDateTime startDate = LocalDate.now().atTime(LocalTime.MIN);
        final LocalDateTime endDate = LocalDate.now().atTime(LocalTime.MAX);
        final NumberOfSoldProductsDto numberOfSoldProductsAvailable = new NumberOfSoldProductsDto(9L);
        final NumberOfSoldProductsDto numberOfSoldProductsOnSale = new NumberOfSoldProductsDto(1L);

        assertEquals(numberOfSoldProductsAvailable, auditService.getNumberOfSoldProducts(startDate, endDate, ProductStatus.AVAILABLE));
        assertEquals(numberOfSoldProductsOnSale, auditService.getNumberOfSoldProducts(startDate, endDate, ProductStatus.ON_SALE));
    }

    @Test
    void getTotalNumberOfSubscribersTest() {
        assertEquals(4, auditService.getTotalNumberOfSubscribers());
    }

    @Test
    void getMostPopularProductsTest() {
        final List<ProductPopularityDto> productPopularityDtos = List.of(new ProductPopularityDto(product1.getId(), product1.getName(), 4L));
        assertEquals(productPopularityDtos, auditService.getMostPopularProducts(1));
    }

}