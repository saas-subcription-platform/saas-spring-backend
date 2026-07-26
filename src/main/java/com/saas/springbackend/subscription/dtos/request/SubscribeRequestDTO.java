package com.saas.springbackend.subscription.dtos.request;

import com.saas.springbackend.company.entity.CompanySize;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscribeRequestDTO {
    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Contact email is required")
    @Email(message = "Invalid email format")
    private String contactEmail;

    @NotNull(message = "Company size is required")
    private CompanySize companySize;

    private String phone;

    private String address;

    private String city;

    private String state;

    private String country;

    private String zipCode;

    private String gstNumber;

    @NotNull(message = "Subscription plan is required")
    private Long planId;

    @NotNull(message = "Pricing option is required")
    private Long pricingId;
}
