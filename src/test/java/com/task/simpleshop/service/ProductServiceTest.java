package com.task.simpleshop.service;

import com.task.simpleshop.db.entity.Product;
import com.task.simpleshop.db.entity.Subscriber;
import com.task.simpleshop.db.repository.ProductRepository;
import com.task.simpleshop.db.repository.SubscriberRepository;
import com.task.simpleshop.exception.ProductNotFoundException;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles({"test"})
@ExtendWith(SpringExtension.class)
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private SubscriberService subscriberService;

    private ProductRequest productRequest1;

    private ProductRequest productRequest2;

    private Product createdProduct1;

    private Product createdProduct2;

    private Subscriber subscriber1;

    private Subscriber subscriber2;

    private Subscriber subscriber3;


    @BeforeEach
    void setUp() throws SubscriberNotFoundException {
        productRequest1 = new ProductRequest();
        productRequest1.setName("Chair");
        productRequest1.setStatus(ProductStatus.ON_SALE);

        productRequest2 = new ProductRequest();
        productRequest2.setName("Table");
        productRequest2.setStatus(ProductStatus.AVAILABLE);

        createdProduct1 = productService.addProduct(productRequest1);
        createdProduct2 = productService.addProduct(productRequest2);


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

        subscriber1 = subscriberService.addSubscriber(subscriberRequest1);
        subscriber2 = subscriberService.addSubscriber(subscriberRequest2);
        subscriber3 = subscriberService.addSubscriber(subscriberRequest3);

        subscriberService.purchaseProduct(subscriber1.getId(), List.of(createdProduct1.getId()));
        subscriberService.purchaseProduct(subscriber2.getId(), List.of(createdProduct1.getId()));
        subscriberService.purchaseProduct(subscriber3.getId(), List.of(createdProduct1.getId()));

    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
        subscriberRepository.deleteAll();
    }

    @Test
    void addProductPositiveTest() {
        final ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Stereo");
        productRequest.setStatus(ProductStatus.AVAILABLE);

        final Product createdProduct = productService.addProduct(productRequest);

        final Product retrievedProduct = productRepository.findById(createdProduct.getId()).get();
        assertEquals(createdProduct.getId(), retrievedProduct.getId());
        assertEquals(createdProduct.getName(), retrievedProduct.getName());
        assertEquals(createdProduct.getStatus(), retrievedProduct.getStatus());
    }

    @Test
    void addProductConflictException() {
        assertThrows(DataIntegrityViolationException.class, () -> {
            productService.addProduct(productRequest1);
        });
    }

    @Test
    void getProductPositiveTest() {
        final Product retrievedProduct = productService.getProduct(createdProduct1.getId());

        assertEquals(createdProduct1.getId(), retrievedProduct.getId());
        assertEquals(createdProduct1.getName(), retrievedProduct.getName());
        assertEquals(createdProduct1.getStatus(), retrievedProduct.getStatus());
    }

    @Test
    void getProductDoesNotExistTest() {
        assertNull(productService.getProduct(999L));
    }

    @Test
    void getAllSubscribersByProductPositiveTest() throws ProductNotFoundException {
        assertEquals(3, productService.getAllSubscribersByProduct(createdProduct1.getId()).size());
    }

    @Test
    void getAllSubscribersByZeroSubscribersProductPositiveTest() throws ProductNotFoundException {
        assertEquals(0, productService.getAllSubscribersByProduct(createdProduct2.getId()).size());
    }

    @Test
    void getAllSubscribersByProductProductDoesNotExistNegativeTest() {
        assertThrows(ProductNotFoundException.class, () -> {
            productService.getAllSubscribersByProduct(999);
        });
    }

    @Test
    void updateProductPositiveTest() throws ProductNotFoundException {
        final ProductRequest productRequest = new ProductRequest();
        final String wooden_chair = "Wooden Chair";
        productRequest.setName(wooden_chair);
        final Product updatedProduct = productService.updateProduct(productRequest, createdProduct1.getId());

        assertEquals(wooden_chair, updatedProduct.getName());
        assertEquals(ProductStatus.ON_SALE, updatedProduct.getStatus());
    }

    @Test
    void updateProductDoesNotExistNegativeTest() {
        final ProductRequest productRequest = new ProductRequest();
        final String wooden_chair = "Wooden Chair";
        productRequest.setName(wooden_chair);

        assertThrows(ProductNotFoundException.class, () -> {
            productService.updateProduct(productRequest, 999);
        });
    }

    @Test
    void getAllProductsTest() {
        assertEquals(2, productService.getAllProducts().size());

    }

    @Test
    void deleteProductPositiveTest() throws ProductNotFoundException {
        productService.deleteProduct(createdProduct1.getId());
        assertEquals(Optional.empty(), productRepository.findById(createdProduct1.getId()));
    }

    @Test
    void deleteProductNegativeTest() {
        assertThrows(ProductNotFoundException.class, () -> {
            productService.deleteProduct(999);
        });
    }

}