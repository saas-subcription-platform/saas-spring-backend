package com.saas.springbackend.employee.leaveplanner.service;

import com.saas.springbackend.employee.leaveplanner.dtos.ApplyLeaveRequestDto;
import com.saas.springbackend.employee.leaveplanner.dtos.LeaveBalanceResponseDto;
import com.saas.springbackend.employee.leaveplanner.dtos.LeaveResponseDto;

import java.util.List;

public interface LeaveService {

    LeaveResponseDto applyLeave(ApplyLeaveRequestDto requestDto);

    List<LeaveResponseDto> getMyLeaves();

    LeaveBalanceResponseDto getLeaveBalance();

    LeaveResponseDto approveLeave(Long leaveRequestId);
}
