package com.task.simpleshop.controller;

import com.task.simpleshop.db.entity.Product;
import com.task.simpleshop.exception.ProductNotFoundException;
import com.task.simpleshop.request.ProductRequest;
import com.task.simpleshop.response.ProductResponse;
import com.task.simpleshop.response.SubscriberResponse;
import com.task.simpleshop.service.ProductService;
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
        value = "/products"
)
public class ProductController {

    private static final String PRODUCT_NOT_FOUND = "Product with id %s is not found";
    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(@RequestBody ProductRequest productRequest) {
        final Product product = productService.addProduct(productRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ProductResponse(product));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Object> getProduct(@PathVariable long productId) {
        final Product product = productService.getProduct(productId);
        return buildProductResponse(product, productId);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Object> updateProduct(@PathVariable long productId, @RequestBody ProductRequest productRequest) throws ProductNotFoundException {

        final Product product = productService.updateProduct(productRequest, productId);
        return buildProductResponse(product, productId);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Object> deleteProduct(@PathVariable long productId) throws ProductNotFoundException {
        productService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.OK.getReasonPhrase(), HttpStatus.OK);

    }

    @GetMapping("/subscribers/{productId}")
    public ResponseEntity<Object> getAcquiredProducts(@PathVariable long productId) throws ProductNotFoundException {
        final List<SubscriberResponse> subscribers = productService.getAllSubscribersByProduct(productId);
        return ResponseEntity.status(HttpStatus.OK).body(subscribers);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getAllProducts());
    }

    private ResponseEntity<Object> buildProductResponse(Product product, long productId) {
        if (product == null) {
            return new ResponseEntity<>(String.format(PRODUCT_NOT_FOUND, productId), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ProductResponse(product));
    }


}
