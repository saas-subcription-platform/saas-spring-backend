package com.saas.springbackend.employee.leaveplanner.controller;

import com.saas.springbackend.employee.leaveplanner.dtos.ApplyLeaveRequestDto;
import com.saas.springbackend.employee.leaveplanner.dtos.LeaveBalanceResponseDto;
import com.saas.springbackend.employee.leaveplanner.dtos.LeaveResponseDto;
import com.saas.springbackend.employee.leaveplanner.service.LeaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee/leaves")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;

    @PostMapping("/apply")
    public ResponseEntity<LeaveResponseDto> applyLeave(
            @Valid @RequestBody ApplyLeaveRequestDto requestDto) {

        LeaveResponseDto response =
                leaveService.applyLeave(requestDto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/history")
    public ResponseEntity<List<LeaveResponseDto>> getMyLeaves() {

        return ResponseEntity.ok(leaveService.getMyLeaves());
    }

    @GetMapping("/balance")
    public ResponseEntity<LeaveBalanceResponseDto> getLeaveBalance() {

        return ResponseEntity.ok(leaveService.getLeaveBalance());
    }

    @PatchMapping("/{leaveRequestId}/approve")
    public ResponseEntity<LeaveResponseDto> approveLeave(
            @PathVariable Long leaveRequestId) {

        LeaveResponseDto response =
                leaveService.approveLeave(leaveRequestId);

        return ResponseEntity.ok(response);
    }
}