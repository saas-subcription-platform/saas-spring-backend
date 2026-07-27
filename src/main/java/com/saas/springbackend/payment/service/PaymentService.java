package com.saas.springbackend.payment.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import com.saas.springbackend.payment.dto.CreatePaymentRequestDto;
import com.saas.springbackend.payment.dto.CreatePaymentResponseDto;
import com.saas.springbackend.payment.dto.VerifyPaymentRequestDto;
import com.saas.springbackend.payment.entity.Payment;
import com.saas.springbackend.payment.entity.PaymentStatus;
import com.saas.springbackend.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final RazorpayClient razorpayClient;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    //To create new payment
    public CreatePaymentResponseDto createPaymentOrder(CreatePaymentRequestDto request) throws RazorpayException {

        int amountInPaise = request.getAmount()
                .multiply(BigDecimal.valueOf(100))
                .intValueExact();
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", "INR");

        Order razorpayOrder = razorpayClient.orders.create(orderRequest);

        Payment payment = Payment.builder()
                .subscriptionId(request.getSubscriptionId())
                .amount(request.getAmount())
                .status(PaymentStatus.PENDING)
                .gatewayOrderId(razorpayOrder.get("id"))
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        return CreatePaymentResponseDto.builder()
                .paymentId(savedPayment.getId())
                .subscriptionId(savedPayment.getSubscriptionId())
                .amount(savedPayment.getAmount())
                .currency("INR")
                .gatewayOrderId(savedPayment.getGatewayOrderId())
                .status(savedPayment.getStatus())
                .build();

    }

    //To verify payment
    public boolean verifyPayment(
            VerifyPaymentRequestDto request) throws RazorpayException {

        // Find our Payment using the Razorpay order ID
        Payment payment = paymentRepository
                .findByGatewayOrderId(request.getRazorpayOrderId())
                .orElseThrow(() -> new RuntimeException("Payment not found"));


        JSONObject data = new JSONObject();

        data.put(
                "razorpay_order_id",
                payment.getGatewayOrderId()
        );

        data.put(
                "razorpay_payment_id",
                request.getRazorpayPaymentId()
        );

        data.put(
                "razorpay_signature",
                request.getRazorpaySignature()
        );

        boolean verified =
                Utils.verifyPaymentSignature(data, keySecret);

        if (verified) {
            payment.setStatus(PaymentStatus.SUCCESS);
            paymentRepository.save(payment);
        }

        return verified;
    }
}
