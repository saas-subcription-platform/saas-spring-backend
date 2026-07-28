package com.saas.springbackend.subscription.controller;

import com.saas.springbackend.subscription.dtos.response.PlanPricingResponseDTO;
import com.saas.springbackend.subscription.service.PlanPricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscription-plans")
@RequiredArgsConstructor
public class PlanPricingController {
    private final PlanPricingService planPricingService;

    /**
     * Retrieves all pricing options for a subscription plan.
     *
     * @param planId Subscription Plan ID.
     * @return List of pricing options.
     */
    @GetMapping("/{planId}/pricing")
    public ResponseEntity<List<PlanPricingResponseDTO>> getPricing(
            @PathVariable Long planId) {

        return ResponseEntity.ok(
                planPricingService.getPricingByPlan(planId));
    }
}
