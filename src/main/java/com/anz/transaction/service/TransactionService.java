package com.anz.transaction.service;

import com.anz.account.entity.AccountEntity;
import com.anz.account.repository.AccountRepository;
import com.anz.common.api.TransactionResponse;
import com.anz.common.exception.NotFoundException;
import com.anz.common.validation.ResponseValidator;
import com.anz.transaction.entity.TransactionEntity;
import com.anz.transaction.mapper.TransactionMapper;
import com.anz.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {
    private final TransactionRepository transactionRepository;

    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;
    private final ResponseValidator responseValidator;

    public TransactionResponse getTransactionsByAccountId(Long accountId, String userId, int page, int size) {
        Optional<AccountEntity> accountEntity = accountRepository.findById(accountId);

        if (accountEntity.isEmpty()) {
            throw new NotFoundException("Requested account is not available");
        }

        Pageable paging = PageRequest.of(page, size, Sort.by("valueDate"));
        Page<TransactionEntity> transactionPage = transactionRepository.findAllByAccountAndAccountUserId(accountEntity.get(), userId, paging);

        log.debug("Account service retrieved {} transactions for {} user and {} account",
                transactionPage.getContent()
                        .size(), userId, accountId);

        TransactionResponse transactionResponse = TransactionResponse.builder()
                .transactions(
                        transactionPage.getContent()
                                .stream()
                                .map(transactionMapper::transactionEntityToTransaction)
                                .collect(Collectors.toList()))
                .build();

        responseValidator.applyValidations(transactionResponse);

        return transactionResponse;
    }
}
