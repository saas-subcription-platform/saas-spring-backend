package com.saas.springbackend.employee.leaveplanner.service;

import com.saas.springbackend.common.exception.InvalidRequestException;
import com.saas.springbackend.common.exception.LeaveValidationException;
import com.saas.springbackend.employee.leaveplanner.dtos.*;
import com.saas.springbackend.employee.leaveplanner.entity.LeaveBalance;
import com.saas.springbackend.employee.leaveplanner.entity.LeaveRequest;
import com.saas.springbackend.employee.leaveplanner.enums.LeaveStatus;
import com.saas.springbackend.employee.leaveplanner.repository.LeaveBalanceRepository;
import com.saas.springbackend.employee.leaveplanner.repository.LeaveRequestRepository;
import com.saas.springbackend.notification.entity.NotificationType;
import com.saas.springbackend.notification.repository.NotificationRepository;
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
import java.time.temporal.ChronoUnit;
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

// 3. Check dates
        if (requestDto.getFromDate().isBefore(LocalDate.now())) {
            throw new InvalidRequestException("Past dates are not allowed.");
        }

        User employee = getLoggedInUser();

        leaveBalanceRepository.findByEmployee(employee)
                .orElseThrow(() ->
                        new InvalidRequestException("Leave balance not found"));

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
                NotificationType.GENERAL,
                savedLeave.getId()
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
                .orElseThrow(() -> new InvalidRequestException("Leave balance not found"));

        return modelMapper.map(leaveBalance, LeaveBalanceResponseDto.class);
    }

    private User getLoggedInUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


    @Override
    public LeaveResponseDto approveLeave(Long leaveRequestId) {


        LeaveRequest leaveRequest = leaveRequestRepository
                .findById(leaveRequestId)
                .orElseThrow(() ->
                        new InvalidRequestException("Leave request not found"));
        User admin = getLoggedInUser();

        if (!leaveRequest.getEmployee()
                .getCompany()
                .getId()
                .equals(admin.getCompany().getId())) {

            throw new InvalidRequestException("Access denied.");
        }

        if (leaveRequest.getStatus() == LeaveStatus.APPROVED) {
            throw new InvalidRequestException("Leave request is already approved.");
        }
        leaveRequest.setStatus(LeaveStatus.APPROVED);

        LeaveRequest updatedLeave =
                leaveRequestRepository.save(leaveRequest);

        User employee = updatedLeave.getEmployee();

        LeaveBalance leaveBalance = leaveBalanceRepository
                .findByEmployee(employee)
                .orElseThrow(() ->
                        new InvalidRequestException("Leave balance not found"));

        long totalDays =
                ChronoUnit.DAYS.between(
                        updatedLeave.getFromDate(),
                        updatedLeave.getToDate()
                ) + 1;

        switch (updatedLeave.getLeaveType()) {

            case CASUAL -> {
                if (leaveBalance.getCasualBalance() < totalDays) {
                    throw new InvalidRequestException("Insufficient casual leave balance.");
                }

                leaveBalance.setCasualBalance(
                        leaveBalance.getCasualBalance() - (int) totalDays
                );
            }

            case SICK -> {
                if (leaveBalance.getSickBalance() < totalDays) {
                    throw new InvalidRequestException("Insufficient sick leave balance.");
                }

                leaveBalance.setSickBalance(
                        leaveBalance.getSickBalance() - (int) totalDays
                );
            }

            case EARNED -> {
                if (leaveBalance.getEarnedBalance() < totalDays) {
                    throw new InvalidRequestException("Insufficient earned leave balance.");
                }

                leaveBalance.setEarnedBalance(
                        leaveBalance.getEarnedBalance() - (int) totalDays
                );
            }
        }

        leaveBalanceRepository.save(leaveBalance);
        LeaveResponseDto response =
                modelMapper.map(updatedLeave, LeaveResponseDto.class);

        response.setLeaveRequestId(updatedLeave.getId());

        return response;
    }
}