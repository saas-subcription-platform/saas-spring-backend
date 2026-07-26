package com.saas.springbackend.subscription.dtos.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanFeatureResponseDTO {
    private Long id;
    private String featureName;
    private String featureDescription;
    private boolean enabled;
}
