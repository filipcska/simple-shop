package com.task.simpleshop.db.repository;

import com.task.simpleshop.db.entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {

    @Query("select count(s) from Subscriber s")
    Long findNumberOfSubscribers();
}
