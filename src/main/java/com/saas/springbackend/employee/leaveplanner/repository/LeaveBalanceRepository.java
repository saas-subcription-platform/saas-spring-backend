package com.saas.springbackend.employee.leaveplanner.repository;

import com.saas.springbackend.employee.leaveplanner.entity.LeaveBalance;
import com.saas.springbackend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {

    Optional<LeaveBalance> findByEmployee(User employee);

}