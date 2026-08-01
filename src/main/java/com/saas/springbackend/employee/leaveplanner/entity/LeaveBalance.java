package com.saas.springbackend.employee.leaveplanner.entity;

import com.saas.springbackend.common.entity.BaseClass;
import com.saas.springbackend.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "leave_balances")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@AttributeOverride(
        name = "id",
        column = @Column(name = "leave_balance_id"))
public class LeaveBalance extends BaseClass {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false, unique = true)
    private User employee;

    @Column(nullable = false)
    private Integer casualBalance;

    @Column(nullable = false)
    private Integer sickBalance;

    @Column(nullable = false)
    private Integer earnedBalance;
}