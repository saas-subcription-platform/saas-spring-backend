package com.saas.springbackend.payment.util;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import com.saas.springbackend.common.exception.UnsupportedPaymentMethodException;
import com.saas.springbackend.payment.dto.VerifyPaymentRequestDto;
import com.saas.springbackend.payment.entity.Payment;
import com.saas.springbackend.transaction.entity.PaymentMethod;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RazorpayPaymentHelper {

    private final RazorpayClient razorpayClient;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    public boolean verifySignature(
            Payment payment,
            VerifyPaymentRequestDto request) throws RazorpayException {

        JSONObject data = new JSONObject();

        data.put("razorpay_order_id", payment.getGatewayOrderId());
        data.put("razorpay_payment_id", request.getRazorpayPaymentId());
        data.put("razorpay_signature", request.getRazorpaySignature());

        return Utils.verifyPaymentSignature(data, keySecret);
    }

    public PaymentMethod getPaymentMethod(
            String razorpayPaymentId) throws RazorpayException {

        com.razorpay.Payment razorpayPayment =
                razorpayClient.payments.fetch(razorpayPaymentId);

        String method = razorpayPayment.get("method");

        if ("upi".equalsIgnoreCase(method)) {
            return PaymentMethod.UPI;
        }

        if ("card".equalsIgnoreCase(method)) {
            return PaymentMethod.CARD;
        }

        throw new UnsupportedPaymentMethodException("Unsupported payment method: " + method);
    }
}