package com.saas.springbackend.subscription.controller;

import com.saas.springbackend.subscription.dtos.response.PlanFeatureResponseDTO;
import com.saas.springbackend.subscription.service.PlanFeatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscription-plans")
@RequiredArgsConstructor
public class PlanFeatureController {
    private final PlanFeatureService planFeatureService;

    /**
     * Retrieves all features of a subscription plan.
     *
     * @param planId Subscription Plan ID.
     * @return List of plan features.
     */
    @GetMapping("/{planId}/features")
    public ResponseEntity<List<PlanFeatureResponseDTO>> getPlanFeatures(
            @PathVariable Long planId) {

        return ResponseEntity.ok(
                planFeatureService.getFeaturesByPlan(planId));
    }


}
