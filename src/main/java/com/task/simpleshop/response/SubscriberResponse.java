package com.task.simpleshop.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.task.simpleshop.db.entity.Subscriber;

import java.time.LocalDateTime;

public record SubscriberResponse(@JsonProperty("subscriber_id") long id,
                                 @JsonProperty("subscriber_name") String name,
                                 @JsonProperty("subscriber_joined_on") @JsonFormat(pattern="dd-MM-yyyy") LocalDateTime createdAt) {

    public SubscriberResponse(Subscriber subscriber) {
        this(subscriber.getId(), subscriber.getFirstName(), subscriber.getCreatedAt());
    }
}
