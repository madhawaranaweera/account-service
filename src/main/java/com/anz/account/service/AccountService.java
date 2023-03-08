package com.anz.account.service;

import com.anz.account.mapper.AccountMapper;
import com.anz.account.repository.AccountRepository;
import com.anz.common.api.Account;
import com.anz.common.api.AccountResponse;
import com.anz.common.exception.NotFoundException;
import com.anz.common.validation.ResponseValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final ResponseValidator responseValidator;

    public AccountResponse getAccountsByUserId(String userId) {
        List<Account> accounts = accountRepository.findAllByUserId(userId)
                .stream()
                .map(accountMapper::accountEntityToAccount)
                .collect(Collectors.toList());

        if (accounts.isEmpty()) {
            log.warn("No accounts available for the given user");
            throw new NotFoundException("No accounts available for the given user");
        }

        log.debug("Account service retrieved {} accounts", accounts.size());

        AccountResponse accountResponse = AccountResponse.builder()
                .accounts(accounts)
                .build();

        responseValidator.applyValidations(accountResponse);
        return accountResponse;
    }
}
