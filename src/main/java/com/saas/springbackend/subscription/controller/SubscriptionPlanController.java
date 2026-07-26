package com.saas.springbackend.subscription.controller;

import com.saas.springbackend.subscription.dtos.request.*;
import com.saas.springbackend.subscription.dtos.response.*;
import com.saas.springbackend.subscription.service.SubscriptionPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/subscription-plans")
@RequiredArgsConstructor
public class SubscriptionPlanController {
    private final SubscriptionPlanService subscriptionPlanService;

    @GetMapping
    public ResponseEntity<List<SubscriptionPlanResponseDTO>> getAllPlans() {

        return ResponseEntity.ok(subscriptionPlanService.getAllPlans());

    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionPlanResponseDTO> getPlanById(
            @PathVariable Long id) {

        return ResponseEntity.ok(subscriptionPlanService.getPlanById(id));

    }
}
