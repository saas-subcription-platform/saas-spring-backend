package com.saas.springbackend.dashboard.dto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardResponseDto {

    private String adminName;
    private String companyName;

}