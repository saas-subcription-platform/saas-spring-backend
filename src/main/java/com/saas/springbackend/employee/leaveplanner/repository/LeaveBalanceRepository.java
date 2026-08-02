package com.saas.springbackend.employee.leaveplanner.repository;

import com.saas.springbackend.employee.leaveplanner.entity.LeaveBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {

    Optional<LeaveBalance> findByEmployeeId(Long employeeId);

    void deleteByEmployeeId(Long employeeId);
}