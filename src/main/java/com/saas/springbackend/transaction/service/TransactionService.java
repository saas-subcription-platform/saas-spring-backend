package com.saas.springbackend.transaction.service;

import com.saas.springbackend.transaction.dto.TransactionResponseDto;
import com.saas.springbackend.transaction.entity.Transaction;
import com.saas.springbackend.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public List<TransactionResponseDto> getAllTransactions() {

        //Fetching transactions from db
        List<Transaction> transactions = transactionRepository.findAll();

        //Converting transaction into dto
        return transactions.stream()
                .map(transaction -> TransactionResponseDto.builder()
                        .transactionId(transaction.getId())
                        .paymentId(transaction.getPayment().getId())
                        .amount(transaction.getAmount())
                        .paymentMethod(transaction.getPaymentMethod())
                        .status(transaction.getStatus())
                        .gatewayPaymentId(transaction.getGatewayPaymentId())
                        .build())
                .toList();
    }
}
