package com.saas.springbackend.transaction.service;

import com.saas.springbackend.payment.entity.Payment;
import com.saas.springbackend.transaction.dto.TransactionResponseDto;
import com.saas.springbackend.transaction.entity.PaymentMethod;
import com.saas.springbackend.transaction.entity.Transaction;
import com.saas.springbackend.transaction.entity.TransactionStatus;
import com.saas.springbackend.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;

    public List<TransactionResponseDto> getAllTransactions() {

        //Fetching transactions from db
        List<Transaction> transactions = transactionRepository.findAll();

        //Converting transaction into dto
        return transactions.stream()
                .map(transaction -> {

                    TransactionResponseDto dto =
                            modelMapper.map(transaction, TransactionResponseDto.class);

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
}
