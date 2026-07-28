package com.saas.springbackend.subscription.controller;

import com.saas.springbackend.subscription.dtos.request.*;
import com.saas.springbackend.subscription.dtos.response.*;
import com.saas.springbackend.subscription.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    /**
     * Creates a new subscription.
     *
     * @param request Subscription request.
     * @return Created subscription details.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<SubscriptionResponseDTO>> subscribe(
            @Valid @RequestBody SubscribeRequestDTO request) {

        SubscriptionResponseDTO response =
                subscriptionService.subscribe(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Subscription created successfully.",
                        response
                ));
    }

    /**
     * Retrieves subscription details by ID.
     *
     * @param subscriptionId Subscription ID.
     * @return Subscription details.
     */
    @GetMapping("/{subscriptionId}")
    public ResponseEntity<ApiResponse<SubscriptionResponseDTO>> getSubscription(
            @PathVariable Long subscriptionId) {

        SubscriptionResponseDTO response =
                subscriptionService.getSubscription(subscriptionId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Subscription retrieved successfully.",
                        response
                )
        );
    }

    @PostMapping("/{subscriptionId}/renew")
    public ResponseEntity<SubscriptionResponseDTO> renewSubscription(
            @PathVariable Long subscriptionId) {

        return ResponseEntity.ok(
                subscriptionService.renewSubscription(subscriptionId)
        );
    }

    @PostMapping("/{subscriptionId}/cancel")
    public ResponseEntity<SubscriptionResponseDTO> cancelSubscription(
            @PathVariable Long subscriptionId) {

        return ResponseEntity.ok(
                subscriptionService.cancelSubscription(subscriptionId)
        );
    }

    @PutMapping("/{subscriptionId}/change-plan")
    public ResponseEntity<SubscriptionResponseDTO> changePlan(
            @PathVariable Long subscriptionId,
            @Valid @RequestBody ChangePlanRequestDTO request) {

        return ResponseEntity.ok(
                subscriptionService.changePlan(
                        subscriptionId,
                        request
                )
        );
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<SubscriptionDetailsResponseDTO>> getMySubscription() {

        SubscriptionDetailsResponseDTO response =
                subscriptionService.getMySubscription();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Subscription retrieved successfully.",
                        response
                )
        );
    }
}
