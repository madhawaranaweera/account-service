package com.anz.account.repository;

import com.anz.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account,String> {
    List<Account> findAllByUserId(String userId);
}
