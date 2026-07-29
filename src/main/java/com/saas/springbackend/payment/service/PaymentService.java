package com.saas.springbackend.payment.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.saas.springbackend.invoice.service.InvoiceService;
import com.saas.springbackend.payment.dto.PaymentFailureRequestDto;
import com.saas.springbackend.transaction.service.TransactionService;
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
                .subscriptionId(request.getSubscriptionId())
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

        Payment payment = paymentRepository
                .findByGatewayOrderId(request.getRazorpayOrderId())
                .orElseThrow(() ->
                        new PaymentNotFoundException("Payment not found")
                );

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            return;
        }

        payment.setStatus(PaymentStatus.FAILED);

        Payment savedPayment = paymentRepository.save(payment);

        transactionService.createFailedTransaction(
                savedPayment,
                request.getRazorpayPaymentId(),
                request.getFailureReason()
        );
    }
}
