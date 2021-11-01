package com.task.simpleshop.db.repository;

import com.task.simpleshop.db.entity.SubscriberProduct;
import com.task.simpleshop.dto.NumberOfSoldProductsDto;
import com.task.simpleshop.status.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface SubscriberProductRepository extends JpaRepository<SubscriberProduct, Long> {

    @Query("""
            select new  com.task.simpleshop.dto.NumberOfSoldProductsDto(count(sp.productId) as number_of_sold_products) 
            from SubscriberProduct sp, Product p 
            where sp.productId = p.id 
            and (sp.createdAt between :startDate and :endDate) 
            and p.status = :status""")
    NumberOfSoldProductsDto findNumberOfSoldProducts(@Param("startDate") LocalDateTime startDate,
                                                     @Param("endDate") LocalDateTime endDate,
                                                     @Param("status") ProductStatus status);
}
