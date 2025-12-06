package com.service.event.repository;

import com.service.event.entity.EventSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventSubscriptionRepository extends JpaRepository<EventSubscription, Integer> {
}
