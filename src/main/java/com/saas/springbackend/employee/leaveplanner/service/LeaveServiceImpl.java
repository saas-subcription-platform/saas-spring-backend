package com.saas.springbackend.employee.leaveplanner.service;

import com.saas.springbackend.common.exception.LeaveValidationException;
import com.saas.springbackend.employee.leaveplanner.dtos.*;
import com.saas.springbackend.employee.leaveplanner.entity.LeaveBalance;
import com.saas.springbackend.employee.leaveplanner.entity.LeaveRequest;
import com.saas.springbackend.employee.leaveplanner.repository.LeaveBalanceRepository;
import com.saas.springbackend.employee.leaveplanner.repository.LeaveRequestRepository;
import com.saas.springbackend.employee.leaveplanner.service.LeaveService;
import com.saas.springbackend.employee.leaveplanner.repository.LeaveRequestRepository;
import com.saas.springbackend.notification.entity.NotificationType;
import com.saas.springbackend.notification.service.NotificationService;
import com.saas.springbackend.user.entity.User;
import com.saas.springbackend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final ModelMapper modelMapper;
    private final LeaveBalanceRepository leaveBalanceRepository;

    @Override
    public LeaveResponseDto applyLeave(ApplyLeaveRequestDto requestDto) {
        if (requestDto.getFromDate().isAfter(requestDto.getToDate())) {
            throw new LeaveValidationException("From date cannot be after To date.");
        }

        if (requestDto.getFromDate().isBefore(LocalDate.now())) {
            throw new LeaveValidationException("Leave balance not found.");
        }

        User employee = getLoggedInUser();

        LeaveRequest leaveRequest = new LeaveRequest();

        leaveRequest.setEmployee(employee);
        leaveRequest.setLeaveType(requestDto.getLeaveType());
        leaveRequest.setFromDate(requestDto.getFromDate());
        leaveRequest.setToDate(requestDto.getToDate());
        leaveRequest.setReason(requestDto.getReason());

        LeaveRequest savedLeave =
                leaveRequestRepository.save(leaveRequest);

        notificationService.createNotification(
                employee,
                "New Leave Request",
                employee.getFirstName() + " " + employee.getLastName()
                        + " applied for " + requestDto.getLeaveType() + " leave.",
                NotificationType.GENERAL
        );

        LeaveResponseDto response = modelMapper.map(savedLeave, LeaveResponseDto.class);

        response.setLeaveRequestId(savedLeave.getId());

        return response;
    }
    @Override
    public List<LeaveResponseDto> getMyLeaves() {

        User employee = getLoggedInUser();

        List<LeaveRequest> leaveRequests =
                leaveRequestRepository.findByEmployeeOrderByCreatedAtDesc(employee);

        return leaveRequests.stream()
                .map(leaveRequest -> {

                    LeaveResponseDto response =
                            modelMapper.map(leaveRequest, LeaveResponseDto.class);

                    response.setLeaveRequestId(leaveRequest.getId());

                    return response;
                })
                .toList();
    }


    @Override
    public LeaveBalanceResponseDto getLeaveBalance() {

        User employee = getLoggedInUser();

        LeaveBalance leaveBalance = leaveBalanceRepository
                .findByEmployee(employee)
                .orElseThrow(() -> new RuntimeException("Leave balance not found"));

        return modelMapper.map(leaveBalance, LeaveBalanceResponseDto.class);
    }

    private User getLoggedInUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


}