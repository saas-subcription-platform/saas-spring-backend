package com.saas.springbackend.payment.controller;

import com.razorpay.RazorpayException;
import com.saas.springbackend.payment.dto.CreatePaymentRequestDto;
import com.saas.springbackend.payment.dto.CreatePaymentResponseDto;
import com.saas.springbackend.payment.dto.VerifyPaymentRequestDto;
import com.saas.springbackend.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/create-order")
    ResponseEntity<?> createPaymentOrder(@Valid @RequestBody CreatePaymentRequestDto requestDto) throws RazorpayException {
        CreatePaymentResponseDto responseDto = paymentService.createPaymentOrder(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/verify")
    ResponseEntity<?> verifyPayment(@Valid @RequestBody VerifyPaymentRequestDto requestDto) throws RazorpayException {
        boolean verified = paymentService.verifyPayment(requestDto);

        if(verified){
            return ResponseEntity.ok("Payment verified successfully");
        }
        return ResponseEntity
                .badRequest()
                .body("Payment verification failed");

    }
}
