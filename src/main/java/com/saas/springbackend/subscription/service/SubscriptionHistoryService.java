package com.saas.springbackend.subscription.service;

import com.saas.springbackend.subscription.entity.Subscription;

public interface SubscriptionHistoryService {

    void recordSubscriptionCreated(Subscription subscription);

    void recordSubscriptionRenewed(Subscription subscription);

    void recordSubscriptionCancelled(Subscription subscription);

    void recordSubscriptionUpgraded(Subscription subscription);

    void recordSubscriptionDowngraded(Subscription subscription);
}
