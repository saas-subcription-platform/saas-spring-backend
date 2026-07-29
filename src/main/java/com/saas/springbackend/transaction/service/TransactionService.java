package com.saas.springbackend.transaction.service;

import com.saas.springbackend.payment.entity.Payment;
import com.saas.springbackend.subscription.entity.Subscription;
import com.saas.springbackend.subscription.repositories.SubscriptionRepository;
import com.saas.springbackend.transaction.dto.TransactionResponseDto;
import com.saas.springbackend.transaction.entity.PaymentMethod;
import com.saas.springbackend.transaction.entity.Transaction;
import com.saas.springbackend.transaction.entity.TransactionStatus;
import com.saas.springbackend.transaction.repository.TransactionRepository;
import com.saas.springbackend.user.entity.User;
import com.saas.springbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    public List<TransactionResponseDto> getAllTransactions() {

        Long subscriptionId = getCurrentCompanySubscriptionId();

        List<Transaction> transactions =
                transactionRepository.findByPaymentSubscriptionId(subscriptionId);

        return transactions.stream()
                .map(transaction -> {

                    TransactionResponseDto dto =
                            modelMapper.map(
                                    transaction,
                                    TransactionResponseDto.class
                            );

                    dto.setTransactionId(transaction.getId());
                    dto.setPaymentId(transaction.getPayment().getId());

                    return dto;
                })
                .toList();
    }

    public void createTransaction(
            Payment payment,
            PaymentMethod paymentMethod,
            String gatewayPaymentId) {

        Transaction transaction = Transaction.builder()
                .payment(payment)
                .amount(payment.getAmount())
                .paymentMethod(paymentMethod)
                .status(TransactionStatus.SUCCESS)
                .gatewayPaymentId(gatewayPaymentId)
                .remarks("Payment verified successfully")
                .build();

        transactionRepository.save(transaction);
    }

    private Long getCurrentCompanySubscriptionId() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        Long companyId = user.getCompany().getId();

        Subscription subscription =
                subscriptionRepository.findByCompanyId(companyId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Subscription not found for company"
                                )
                        );

        return subscription.getId();
    }
}