package com.saas.springbackend.employee.leaveplanner.dtos;

import lombok.Data;

@Data
public class LeaveBalanceResponseDto {

    private Integer casualBalance;

    private Integer sickBalance;

    private Integer earnedBalance;

}