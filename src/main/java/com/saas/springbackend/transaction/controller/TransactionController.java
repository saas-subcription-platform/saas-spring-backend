package com.saas.springbackend.transaction.controller;

import com.saas.springbackend.transaction.dto.TransactionResponseDto;
import com.saas.springbackend.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<?> getAllTransactions(){
        List<TransactionResponseDto> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }
}
