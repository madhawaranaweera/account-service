package com.anz.account.service;

import com.anz.account.api.AccountDto;
import com.anz.account.api.AccountResponse;
import com.anz.account.controller.AccountController;
import com.anz.account.mapper.AccountMapper;
import com.anz.account.repository.AccountRepository;
import com.anz.account.validation.ResponseValidator;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@Builder
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final ResponseValidator responseValidator;

    public AccountResponse getAccountsByUserId(String userId) {
        List<AccountDto> accountDtos = accountRepository.findAllByUserId(userId)
                .stream()
                .map(accountMapper::accountToAccountDto)
                .collect(Collectors.toList());
        log.debug("Account service retrieved {} accounts for {} user", accountDtos.size(), userId);
        addTransactionLinks(accountDtos, userId);
        AccountResponse accountResponse = AccountResponse.builder()
                .accounts(accountDtos)
                .build();
        responseValidator.applyValidations(accountResponse);
        return accountResponse;
    }

    private void addTransactionLinks(List<AccountDto> accountDtos, String userId) {
        accountDtos.stream()
                .peek(accountDto -> accountDto.add(linkTo(methodOn(AccountController.class)
                        .getTransactionsByAccountId(userId, accountDto.getAccountId(), 0, 50))
                        .withRel("transactions")))
                .collect(Collectors.toList());
    }
}
