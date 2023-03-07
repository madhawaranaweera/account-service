package com.anz.account.service;

import com.anz.account.api.*;
import com.anz.account.entity.Account;
import com.anz.account.entity.Transaction;
import com.anz.account.mapper.TransactionMapper;
import com.anz.account.repository.TransactionRepository;
import com.anz.account.validation.ResponseValidator;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    private static Stream<Arguments> getTransactionsByAccountId_success() {
        return Stream.of(
                Arguments.of(List.of(buildTransactionDto()), 1, List.of(buildTransaction())),
                Arguments.of(List.of(buildTransactionDto(), buildTransactionDto()), 2, List.of(buildTransaction(), buildTransaction())),
                Arguments.of(new ArrayList<Account>(), 0, new ArrayList<AccountDto>())
        );
    }

    private static Account buildAccount() {
        return Account.builder()
                .accountNumber("800200700")
                .accountName("Madhawa")
                .accountType(AccountType.Current)
                .userId("123456")
                .balanceDate(LocalDate.of(2023, 01, 01))
                .currency("AUD")
                .openingAvailableBalance(BigDecimal.valueOf(1001.50))
                .build();
    }

    private static Transaction buildTransaction() {
        return Transaction.builder()
                .id(123L)
                .account(buildAccount())
                .valueDate(LocalDate.of(2023, 01, 1))
                .currency("AUD")
                .amount(BigDecimal.valueOf(1234.50))
                .creditDebitIndicator(CreditDebitIndicator.CREDIT)
                .transactionNarrative("Financial Activity")
                .build();
    }

    private static TransactionDto buildTransactionDto() {
        return TransactionDto.builder()
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
    @MethodSource
    public void getTransactionsByAccountId_success(List<TransactionDto> transactionDtoList, int expectedSize, List<Transaction> transactionList) {
        TransactionResponse expectedResponse = TransactionResponse.builder()
                .transactions(transactionDtoList)
                .build();
        Page<Transaction> pageResult = new PageImpl<>(transactionList, PageRequest.of(1, 50, Sort.by("valueDate")), 50);

        when(transactionRepository.findByAccountIdAndAccountUserId(any(), any(), any())).thenReturn(pageResult);
        when(responseValidator.applyValidations(any())).thenReturn(expectedResponse);

        if (expectedSize != 0) {
            when(transactionMapper.transactionToTransactionDto(any())).thenReturn(transactionDtoList.get(0));
        }

        TransactionResponse actualResponse = transactionService.getTransactionsByAccountId(123456789L, "123456", 1, 50);

        verify(transactionRepository, times(1)).findByAccountIdAndAccountUserId(any(), any(), any());
        assertEquals(expectedSize, actualResponse.getTransactions()
                .size());

        if (expectedSize != 0) {
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
    }


}