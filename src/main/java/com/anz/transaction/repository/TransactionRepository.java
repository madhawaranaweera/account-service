package com.anz.transaction.repository;

import com.anz.account.entity.AccountEntity;
import com.anz.transaction.entity.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    Page<TransactionEntity> findAllByAccountAndAccountUserId(AccountEntity account, String UserId, Pageable pageable);
}
