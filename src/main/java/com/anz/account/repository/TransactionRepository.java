package com.anz.account.repository;

import com.anz.account.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findByAccountIdAndAccountUserId(Long accountId, String userId, Pageable pageable);
}
