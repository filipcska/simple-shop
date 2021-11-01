package com.task.simpleshop.service;

import com.task.simpleshop.db.entity.Product;
import com.task.simpleshop.db.entity.Subscriber;
import com.task.simpleshop.db.repository.ProductRepository;
import com.task.simpleshop.exception.ProductNotFoundException;
import com.task.simpleshop.request.ProductRequest;
import com.task.simpleshop.response.SubscriberResponse;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ProductService {

    private ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product addProduct(ProductRequest request) {
        Product product = new Product();
        ModelMapper mapper = new ModelMapper();

        mapper.map(request, product);
        return productRepository.save(product);
    }

    public Product getProduct(long id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<SubscriberResponse> getAllSubscribersByProduct(long productId) throws ProductNotFoundException {
        final Product product = productRepository.findById(productId).orElseThrow(() ->
                new ProductNotFoundException(String.format("Product with id %s is not found", productId)));

        final Set<Subscriber> productList = product.getSubscribers();

        return productList.stream().
                map(SubscriberResponse::new).toList();
    }

    public Product updateProduct(ProductRequest productRequest, long productId) throws ProductNotFoundException {
        final Product product = productRepository.findById(productId).orElseThrow(() ->
                new ProductNotFoundException(String.format("Product with id %s is not found", productId)));

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        modelMapper.map(productRequest, product);

        return productRepository.save(product);

    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public void deleteProduct(long productId) throws ProductNotFoundException {
        final Product product = productRepository.findById(productId).orElseThrow(() ->
                new ProductNotFoundException(String.format("Product with id %s is not found", productId)));

        productRepository.delete(product);


    }


}
