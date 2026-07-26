package com.saas.springbackend.subscription.dtos.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionPlanResponseDTO {
    private Long id;

    private String planName;

    private String planDescription;

    private Integer maximumUsers;

    private boolean active;

}
