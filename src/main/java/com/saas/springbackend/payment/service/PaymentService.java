package com.saas.springbackend.payment.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.saas.springbackend.invoice.service.InvoiceService;
import com.saas.springbackend.payment.dto.PaymentFailureRequestDto;
import com.saas.springbackend.subscription.entity.Subscription;
import com.saas.springbackend.subscription.repositories.SubscriptionRepository;
import com.saas.springbackend.transaction.service.TransactionService;
import com.saas.springbackend.user.entity.User;
import com.saas.springbackend.user.repository.UserRepository;
import org.json.JSONObject;
import com.saas.springbackend.common.exception.PaymentNotFoundException;
import com.saas.springbackend.payment.dto.CreatePaymentRequestDto;
import com.saas.springbackend.payment.dto.CreatePaymentResponseDto;
import com.saas.springbackend.payment.dto.VerifyPaymentRequestDto;
import com.saas.springbackend.payment.entity.Payment;
import com.saas.springbackend.payment.entity.PaymentStatus;
import com.saas.springbackend.payment.repository.PaymentRepository;
import com.saas.springbackend.payment.util.RazorpayPaymentHelper;
import com.saas.springbackend.transaction.entity.PaymentMethod;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final RazorpayClient razorpayClient;
    private final TransactionService transactionService;
    private final RazorpayPaymentHelper razorpayPaymentHelper;
    private final ModelMapper modelMapper;
    private final InvoiceService invoiceService;
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;


    //To create new payment
    public CreatePaymentResponseDto createPaymentOrder(CreatePaymentRequestDto request) throws RazorpayException {

        int amountInPaise = request.getAmount()
                .multiply(BigDecimal.valueOf(100))
                .setScale(0, RoundingMode.HALF_UP)
                .intValue();
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", "INR");

        Order razorpayOrder = razorpayClient.orders.create(orderRequest);

        Payment payment = Payment.builder()
//                .subscriptionId(request.getSubscriptionId())
                .amount(request.getAmount())
                .status(PaymentStatus.PENDING)
                .gatewayOrderId(razorpayOrder.get("id"))
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        CreatePaymentResponseDto response =
                modelMapper.map(savedPayment, CreatePaymentResponseDto.class);

        response.setPaymentId(savedPayment.getId());
        response.setCurrency("INR");

        return response;

    }

    //To verify payment
    @Transactional
    public boolean verifyPayment(
            VerifyPaymentRequestDto request) throws RazorpayException {

        Payment payment = paymentRepository
                .findByGatewayOrderId(request.getRazorpayOrderId())
                .orElseThrow(() ->
                        new PaymentNotFoundException("Payment not found")
                );

        boolean verified =
                razorpayPaymentHelper.verifySignature(payment, request);

        if (!verified) {
            return false;
        }

        PaymentMethod paymentMethod =
                razorpayPaymentHelper.getPaymentMethod(request.getRazorpayPaymentId());

        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaymentMethod(paymentMethod);

        Payment savedPayment = paymentRepository.save(payment);

        transactionService.createTransaction(
                savedPayment,
                paymentMethod,
                request.getRazorpayPaymentId()
        );

        invoiceService.createInvoice(savedPayment);
        return true;
    }


    //Failure handling
    @Transactional
    public void markPaymentFailed(PaymentFailureRequestDto request) {
        System.out.println("===== markPaymentFailed() CALLED =====");
        System.out.println(request);
        Payment payment = paymentRepository
                .findByGatewayOrderId(request.getRazorpayOrderId())
                .orElseThrow(() ->
                        new PaymentNotFoundException("Payment not found")
                );

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            return;
        }

        // Assign current subscription if payment doesn't already have one
        if (payment.getSubscriptionId() == null) {

            Long subscriptionId = getCurrentCompanySubscriptionId();

            payment.setSubscriptionId(subscriptionId);
        }


        payment.setStatus(PaymentStatus.FAILED);

        System.out.println("Updating payment status to FAILED");

        Payment savedPayment = paymentRepository.save(payment);

        transactionService.createFailedTransaction(
                savedPayment,
                request.getRazorpayPaymentId(),
                request.getFailureReason()
        );

        System.out.println("Failed transaction created");
    }

    private Long getCurrentCompanySubscriptionId() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Long companyId = user.getCompany().getId();

        Subscription subscription =
                subscriptionRepository.findByCompanyId(companyId)
                        .orElseThrow(() ->
                                new RuntimeException("Subscription not found"));

        return subscription.getId();
    }
}
