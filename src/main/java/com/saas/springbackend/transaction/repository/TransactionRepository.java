package com.saas.springbackend.transaction.repository;

import com.saas.springbackend.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
