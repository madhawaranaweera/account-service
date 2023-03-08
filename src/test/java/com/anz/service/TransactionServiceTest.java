package com.anz.service;

import com.anz.account.entity.AccountEntity;
import com.anz.account.repository.AccountRepository;
import com.anz.common.api.*;
import com.anz.common.exception.NotFoundException;
import com.anz.common.exception.ResponseValidationException;
import com.anz.common.validation.ResponseValidator;
import com.anz.transaction.entity.TransactionEntity;
import com.anz.transaction.mapper.TransactionMapper;
import com.anz.transaction.repository.TransactionRepository;
import com.anz.transaction.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private ResponseValidator responseValidator;

    @Mock
    private AccountRepository accountRepository;

    private static Stream<Arguments> givenValidAccountId_getTransactionsByAccountId_returnSuccess() {
        return Stream.of(
                Arguments.of(List.of(buildTransaction()), 1, List.of(buildTransactionEntity())),
                Arguments.of(List.of(buildTransaction(), buildTransaction()), 2, List.of(buildTransactionEntity(), buildTransactionEntity()))
        );
    }

    private static AccountEntity buildAccountEntity() {
        return AccountEntity.builder()
                .accountNumber("800200700")
                .accountName("Madhawa")
                .accountType(AccountType.Current)
                .userId("123456")
                .balanceDate(LocalDate.of(2023, 01, 01))
                .currency("AUD")
                .openingAvailableBalance(BigDecimal.valueOf(1001.50))
                .build();
    }

    private static TransactionEntity buildTransactionEntity() {
        return TransactionEntity.builder()
                .id(123L)
                .account(buildAccountEntity())
                .valueDate(LocalDate.of(2023, 01, 1))
                .currency("AUD")
                .amount(BigDecimal.valueOf(1234.50))
                .creditDebitIndicator(CreditDebitIndicator.CREDIT)
                .transactionNarrative("Financial Activity")
                .build();
    }

    private static Transaction buildTransaction() {
        return Transaction.builder()
                .accountNumber("800200700")
                .accountName("Madhawa")
                .currency("AUD")
                .valueDate(LocalDate.of(2023, 01, 1))
                .creditAmount(Amount.of("AUD", BigDecimal.valueOf(1234.50)))
                .debitAmount(Amount.of("AUD", BigDecimal.ZERO))
                .creditDebitIndicator(CreditDebitIndicator.CREDIT)
                .transactionNarrative("Financial Activity")
                .build();
    }

    @ParameterizedTest()
    @MethodSource("givenValidAccountId_getTransactionsByAccountId_returnSuccess")
    void givenValidAccountId_getTransactionsByAccountId_returnSuccess(List<Transaction> transactionList, int expectedSize, List<TransactionEntity> transactionEntityList) {
        TransactionResponse expectedResponse = TransactionResponse.builder()
                .transactions(transactionList)
                .build();
        Page<TransactionEntity> pageResult = new PageImpl<>(transactionEntityList, PageRequest.of(1, 50, Sort.by("valueDate")), 50);

        when(accountRepository.findById(any())).thenReturn(Optional.of(AccountEntity.builder()
                .userId("123456")
                .id(123456789L)
                .build()));
        when(transactionRepository.findAllByAccountAndAccountUserId(any(),any(),any())).thenReturn(pageResult);
        when(responseValidator.applyValidations(any())).thenReturn(expectedResponse);
        when(transactionMapper.transactionEntityToTransaction(any())).thenReturn(transactionList.get(0));

        TransactionResponse actualResponse = transactionService.getTransactionsByAccountId(123456789L, "123456", 1, 50);

        verify(transactionRepository, times(1)).findAllByAccountAndAccountUserId(any(),any(),any());
        assertEquals(expectedSize, actualResponse.getTransactions()
                .size());

        assertEquals(expectedResponse.getTransactions()
                .get(0)
                .getAccountNumber(), actualResponse.getTransactions()
                .get(0)
                .getAccountNumber());
        assertEquals(expectedResponse.getTransactions()
                .get(0)
                .getAccountName(), actualResponse.getTransactions()
                .get(0)
                .getAccountName());
        assertEquals(expectedResponse.getTransactions()
                .get(0)
                .getTransactionNarrative(), actualResponse.getTransactions()
                .get(0)
                .getTransactionNarrative());
        assertEquals(expectedResponse.getTransactions()
                .get(0)
                .getCurrency(), actualResponse.getTransactions()
                .get(0)
                .getCurrency());
        assertEquals(expectedResponse.getTransactions()
                .get(0)
                .getCreditAmount(), actualResponse.getTransactions()
                .get(0)
                .getCreditAmount());
        assertEquals(expectedResponse.getTransactions()
                .get(0)
                .getDebitAmount(), actualResponse.getTransactions()
                .get(0)
                .getDebitAmount());
        assertEquals(expectedResponse.getTransactions()
                .get(0)
                .getCreditDebitIndicator(), actualResponse.getTransactions()
                .get(0)
                .getCreditDebitIndicator());
        assertEquals(expectedResponse.getTransactions()
                .get(0)
                .getValueDate(), actualResponse.getTransactions()
                .get(0)
                .getValueDate());
    }

    @Test
    void givenInvalidAccountId_getTransactionsByAccountId_throwHttpNotFoundError() {
        when(accountRepository.findById(any())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> transactionService.getTransactionsByAccountId(1L, "123456", 0, 50));
        assertEquals(NotFoundException.class, exception.getClass());
    }

    @Test
    void givenInvalidAccountId_getTransactionsByAccountId_throwResponseValidationError() {
        Page<TransactionEntity> pageResult = new PageImpl<>(List.of(buildTransactionEntity()), PageRequest.of(1, 50, Sort.by("valueDate")), 50);

        when(accountRepository.findById(any())).thenReturn(Optional.of(AccountEntity.builder()
                .userId("123456")
                .id(123456789L)
                .build()));
        when(transactionRepository.findAllByAccountAndAccountUserId(any(),any(),any())).thenReturn(pageResult);
        when(responseValidator.applyValidations(any())).thenThrow(ResponseValidationException.class);
        when(transactionMapper.transactionEntityToTransaction(any())).thenReturn(buildTransaction());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> transactionService.getTransactionsByAccountId(1L, "123456", 0, 50));
        assertEquals(ResponseValidationException.class, exception.getClass());
    }
}