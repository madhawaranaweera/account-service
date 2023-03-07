package com.anz.account.service;

import com.anz.account.api.TransactionResponse;
import com.anz.account.controller.AccountController;
import com.anz.account.entity.Transaction;
import com.anz.account.mapper.TransactionMapper;
import com.anz.account.repository.AccountRepository;
import com.anz.account.repository.TransactionRepository;
import com.anz.account.validation.ResponseValidator;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@Builder
@RequiredArgsConstructor
@Slf4j
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;
    private final ResponseValidator responseValidator;

    public TransactionResponse getTransactionsByAccountId(Long accountId, String userId, int page, int size) {
        Pageable paging = PageRequest.of(page, size, Sort.by("valueDate"));
        Page<Transaction> transactionPage = transactionRepository.findByAccountIdAndAccountUserId(accountId, userId, paging);

        log.debug("Account service retrieved {} transactions for {} user and {} account",
                transactionPage.getContent()
                        .size(), userId, accountId);

        TransactionResponse transactionResponse = TransactionResponse.builder()
                .transactions(
                        transactionPage.getContent()
                                .stream()
                                .map(transactionMapper::transactionToTransactionDto)
                                .collect(Collectors.toList()))
                .build();
        addAccountLinks(transactionResponse, userId);
        responseValidator.applyValidations(transactionResponse);

        return transactionResponse;
    }

    private void addAccountLinks(TransactionResponse transactionResponse, String userId) {
        transactionResponse.add(linkTo(methodOn(AccountController.class)
                .getAccountsByUserId(userId))
                .withRel("accounts"));
    }
}
