package com.task.simpleshop.db.repository;

import com.task.simpleshop.dto.ProductPopularityDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AuditRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;

    public List<ProductPopularityDto> findOrderedBySeatNumberLimitedTo(int limit) {
        return entityManager.createQuery("""
                        select new com.task.simpleshop.dto.ProductPopularityDto(sp.productId, p.name, count(sp.subscriberId) as times_purchased)
                        from Product p, SubscriberProduct sp
                        where p.id = sp.productId
                        group by sp.productId
                        order by times_purchased desc
                        """,
                ProductPopularityDto.class).setMaxResults(limit).getResultList();
    }
}
