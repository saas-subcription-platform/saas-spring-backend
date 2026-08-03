package com.saas.springbackend.employee.leaveplanner.entity;

import com.saas.springbackend.common.entity.BaseClass;
import com.saas.springbackend.employee.leaveplanner.enums.LeaveStatus;
import com.saas.springbackend.employee.leaveplanner.enums.LeaveType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "leave_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@AttributeOverride(
        name = "id",
        column = @Column(name = "leave_request_id"))
public class LeaveRequest extends BaseClass {

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveType leaveType;

    @Column(nullable = false)
    private LocalDate fromDate;

    @Column(nullable = false)
    private LocalDate toDate;

    @Column(nullable = false, length = 500)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private LeaveStatus status = LeaveStatus.PENDING;

    @Column(name = "reviewed_by")
    private Long reviewedBy;

    private LocalDateTime reviewedAt;

    private String remarks;
}