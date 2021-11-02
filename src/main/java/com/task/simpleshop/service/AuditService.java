package com.task.simpleshop.service;

import com.task.simpleshop.db.repository.AuditRepositoryImpl;
import com.task.simpleshop.db.repository.SubscriberProductRepository;
import com.task.simpleshop.db.repository.SubscriberRepository;
import com.task.simpleshop.dto.NumberOfSoldProductsDto;
import com.task.simpleshop.dto.ProductPopularityDto;
import com.task.simpleshop.status.ProductStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditService {

    private SubscriberRepository subscriberRepository;

    private AuditRepositoryImpl auditRepository;

    private SubscriberProductRepository subscriberProductRepository;

    @Autowired
    public AuditService(SubscriberRepository subscriberRepository, AuditRepositoryImpl auditRepository, SubscriberProductRepository subscriberProductRepository) {
        this.subscriberRepository = subscriberRepository;
        this.auditRepository = auditRepository;
        this.subscriberProductRepository = subscriberProductRepository;
    }

    /*
     * Number of sold products with filters for date and for the active field.
     */
    public NumberOfSoldProductsDto getNumberOfSoldProducts(LocalDateTime startDate, LocalDateTime endDate, ProductStatus status) {
        return subscriberProductRepository.findNumberOfSoldProducts(startDate, endDate, status);
    }

    public Long getTotalNumberOfSubscribers() {
        return subscriberRepository.findNumberOfSubscribers();
    }

    public List<ProductPopularityDto> getMostPopularProducts(int limit) {
        return auditRepository.findMostPopularProductsLimitedTo(limit);
    }


}
