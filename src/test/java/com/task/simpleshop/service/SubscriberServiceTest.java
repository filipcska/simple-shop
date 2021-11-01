package com.task.simpleshop.service;

import com.task.simpleshop.db.entity.Product;
import com.task.simpleshop.db.entity.Subscriber;
import com.task.simpleshop.db.repository.ProductRepository;
import com.task.simpleshop.db.repository.SubscriberRepository;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles({"test"})
@ExtendWith(SpringExtension.class)
class SubscriberServiceTest {

    @Autowired
    private SubscriberService subscriberService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SubscriberRepository subscriberRepository;

    private Subscriber createdSubscriber1;

    private List<Product> productsForSubscriber1;

    @BeforeEach
    void setUp() {
        SubscriberRequest subscriberRequest = new SubscriberRequest();
        subscriberRequest.setFirstName("Amber");
        subscriberRequest.setLastName("Heard");

        createdSubscriber1 = subscriberService.addSubscriber(subscriberRequest);

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

        final Product product1 = productService.addProduct(productRequest1);
        final Product product2 = productService.addProduct(productRequest2);
        final Product product3 = productService.addProduct(productRequest3);
        final Product product4 = productService.addProduct(productRequest4);

        productsForSubscriber1 = List.of(product1, product2, product3, product4);


    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
        subscriberRepository.deleteAll();
    }

    @Test
    void addSubscriberTest() {
        SubscriberRequest subscriberRequest = new SubscriberRequest();
        subscriberRequest.setFirstName("Amber");
        subscriberRequest.setLastName("Heard");

        final Subscriber createdSubscriber = subscriberService.addSubscriber(subscriberRequest);

        final Subscriber retrievedSubscriber = subscriberRepository.findById(createdSubscriber.getId()).get();

        assertEquals(createdSubscriber.getId(), retrievedSubscriber.getId());
        assertEquals(createdSubscriber.getFirstName(), retrievedSubscriber.getFirstName());
        assertEquals(createdSubscriber.getLastName(), retrievedSubscriber.getLastName());

    }

    @Test
    void getSubscriberPositiveTest() throws SubscriberNotFoundException {
        final Subscriber retrievedSubscriber = subscriberService.getSubscriber(createdSubscriber1.getId());

        assertEquals(createdSubscriber1.getId(), retrievedSubscriber.getId());
        assertEquals(createdSubscriber1.getFirstName(), retrievedSubscriber.getFirstName());
        assertEquals(createdSubscriber1.getLastName(), retrievedSubscriber.getLastName());
    }

    @Test
    void getSubscriberDoesNotExistNegativeTest() {
        assertThrows(SubscriberNotFoundException.class, () -> {
            subscriberService.getSubscriber(999L);
        });

    }

    @Test
    void getAllProductsBySubscriberPositiveTest() throws SubscriberNotFoundException {
        subscriberService.purchaseProduct(createdSubscriber1.getId(), productsForSubscriber1.stream().map(Product::getId).toList());
        assertEquals(4, subscriberService.getAllProductsBySubscriber(createdSubscriber1.getId()).size());
    }

    @Test
    void getAllProductsBySubscriberDoesNotExistNegativeTest() {
        assertThrows(SubscriberNotFoundException.class, () -> {
            subscriberService.getAllProductsBySubscriber(999L);
        });
    }

    @Test
    void updateSubscriberPositionTest() throws SubscriberNotFoundException {
        SubscriberRequest subscriberRequest = new SubscriberRequest();
        subscriberRequest.setFirstName("Curtis");
        subscriberRequest.setLastName("Joseph");

        final Subscriber updatedSubscriber = subscriberService.updateSubscriber(subscriberRequest, createdSubscriber1.getId());
        assertEquals(updatedSubscriber.getFirstName(), updatedSubscriber.getFirstName());
        assertEquals(updatedSubscriber.getLastName(), updatedSubscriber.getLastName());
    }

    @Test
    void updateSubscriberDoesNotExistNegativeTest() {
        SubscriberRequest subscriberRequest = new SubscriberRequest();
        subscriberRequest.setFirstName("Curtis");
        subscriberRequest.setLastName("Joseph");

        assertThrows(SubscriberNotFoundException.class, () -> {
            subscriberService.updateSubscriber(subscriberRequest, 999L);
        });

    }


    @Test
    void deleteSubscriberPositiveTest() throws SubscriberNotFoundException {
        subscriberService.deleteSubscriber(createdSubscriber1.getId());
        assertEquals(Optional.empty(), subscriberRepository.findById(createdSubscriber1.getId()));
    }

    @Test
    void getAllSubscribers() throws SubscriberNotFoundException {
        SubscriberRequest subscriberRequest1 = new SubscriberRequest();
        subscriberRequest1.setFirstName("Amber");
        subscriberRequest1.setLastName("Heard");

        SubscriberRequest subscriberRequest2 = new SubscriberRequest();
        subscriberRequest2.setFirstName("Amber");
        subscriberRequest2.setLastName("Heard");

        Subscriber localCreatedSubscriber1 = subscriberService.addSubscriber(subscriberRequest1);
        Subscriber localCreatedSubscriber2 = subscriberService.addSubscriber(subscriberRequest2);

        subscriberService.purchaseProduct(createdSubscriber1.getId(), List.of(localCreatedSubscriber1.getId(), localCreatedSubscriber2.getId()));

        assertEquals(3, subscriberService.getAllSubscribers().size());
    }

    @Test
    void purchaseProductPositiveTest() throws SubscriberNotFoundException {
        assertEquals(4, subscriberService.purchaseProduct(createdSubscriber1.getId(),
                productsForSubscriber1.stream().map(Product::getId).toList()).productName().size());
    }

    @Test
    void purchaseProductSubscriberDoesNotExistNegativeTest() {
        assertThrows(SubscriberNotFoundException.class, () -> {
            subscriberService.purchaseProduct(999L, productsForSubscriber1.stream().map(Product::getId).toList());
        });
    }
}