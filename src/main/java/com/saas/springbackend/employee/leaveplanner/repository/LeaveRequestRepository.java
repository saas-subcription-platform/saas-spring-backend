package com.saas.springbackend.employee.leaveplanner.repository;

import com.saas.springbackend.employee.leaveplanner.entity.LeaveRequest;
import com.saas.springbackend.employee.leaveplanner.entity.LeaveRequest;
import com.saas.springbackend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findByEmployeeOrderByCreatedAtDesc(User employee);

}