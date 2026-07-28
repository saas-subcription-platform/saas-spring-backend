package com.saas.springbackend.subscription.service;

import com.saas.springbackend.subscription.entity.Subscription;
import com.saas.springbackend.subscription.entity.SubscriptionAction;
import com.saas.springbackend.subscription.entity.SubscriptionHistory;
import com.saas.springbackend.subscription.repositories.SubscriptionHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionHistoryServiceImpl implements  SubscriptionHistoryService{
    private final SubscriptionHistoryRepository subscriptionHistoryRepository;

    @Override
    public void recordSubscriptionCreated(Subscription subscription) {

        saveHistory(
                subscription,
                SubscriptionAction.SUBSCRIBED
        );
    }

    @Override
    public void recordSubscriptionRenewed(Subscription subscription) {

        saveHistory(
                subscription,
                SubscriptionAction.RENEWED
        );
    }

    @Override
    public void recordSubscriptionCancelled(Subscription subscription) {

        saveHistory(
                subscription,
                SubscriptionAction.CANCELLED
        );
    }

    @Override
    public void recordSubscriptionUpgraded(Subscription subscription, String oldPlan, String newPlan) {

        saveHistory(subscription, SubscriptionAction.UPGRADED, oldPlan, newPlan);
    }

    @Override
    public void recordSubscriptionDowngraded(Subscription subscription, String oldPlan, String newPlan) {

        saveHistory(subscription, SubscriptionAction.DOWNGRADED, oldPlan, newPlan);
    }

    private void saveHistory(
            Subscription subscription,
            SubscriptionAction action,
            String oldPlan,
            String newPlan) {

        SubscriptionHistory history =
                SubscriptionHistory.builder()
                        .subscription(subscription)
                        .action(action)
                        .oldPlan(oldPlan)
                        .newPlan(newPlan)
                        .amountPaid(subscription.getAmount())
                        .remarks(action.name())
                        .build();

        subscriptionHistoryRepository.save(history);
    }

    private void saveHistory(
            Subscription subscription,
            SubscriptionAction action) {

        SubscriptionHistory history =
                SubscriptionHistory.builder()
                        .subscription(subscription)
                        .action(action)
                        .newPlan(subscription.getSubscriptionPlan().getPlanName())
                        .amountPaid(subscription.getAmount())
                        .remarks(action.name())
                        .build();

        subscriptionHistoryRepository.save(history);
    }

}
