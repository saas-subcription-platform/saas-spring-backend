package com.saas.springbackend.payment.repository;

import com.saas.springbackend.payment.entity.Payment;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByGatewayOrderId(@NotBlank(message = "Razorpay order ID is required") String razorpayOrderId);
}
