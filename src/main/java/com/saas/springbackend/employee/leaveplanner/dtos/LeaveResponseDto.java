package com.saas.springbackend.employee.leaveplanner.dtos;

import com.saas.springbackend.employee.leaveplanner.enums.LeaveStatus;
import com.saas.springbackend.employee.leaveplanner.enums.LeaveType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LeaveResponseDto {

    private Long leaveRequestId;

    private LeaveType leaveType;

    private LocalDate fromDate;

    private LocalDate toDate;

    private String reason;

    private LeaveStatus status;
}