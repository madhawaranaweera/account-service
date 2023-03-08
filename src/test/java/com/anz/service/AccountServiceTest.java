package com.anz.service;

import com.anz.account.entity.AccountEntity;
import com.anz.account.mapper.AccountMapper;
import com.anz.account.repository.AccountRepository;
import com.anz.account.service.AccountService;
import com.anz.common.api.Account;
import com.anz.common.api.AccountResponse;
import com.anz.common.api.AccountType;
import com.anz.common.api.Amount;
import com.anz.common.exception.NotFoundException;
import com.anz.common.exception.ResponseValidationException;
import com.anz.common.validation.ResponseValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private ResponseValidator responseValidator;

    private static Stream<Arguments> givenValidUserId_getAccountsByUserId_returnSuccess() {
        return Stream.of(
                Arguments.of(List.of(buildAccountEntity()), 1, List.of(buildAccount())),
                Arguments.of(List.of(buildAccountEntity(), buildAccountEntity()), 2, List.of(buildAccount(), buildAccount()))
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

    private static Account buildAccount() {
        return Account.builder()
                .accountNumber("800200700")
                .accountName("Madhawa")
                .accountType(AccountType.Current)
                .balanceDate(LocalDate.of(2023, 01, 01))
                .currency("AUD")
                .openingAvailableBalance(Amount.of("AUD", BigDecimal.valueOf(1001.50)))
                .build();
    }

    @ParameterizedTest()
    @MethodSource("givenValidUserId_getAccountsByUserId_returnSuccess")
    void givenValidUserId_getAccountsByUserId_returnSuccess(List<AccountEntity> accountEntityList, int expectedSize, List<Account> accountList) {
        AccountResponse expectedResponse = AccountResponse.builder()
                .accounts(accountList)
                .build();

        when(accountRepository.findAllByUserId(any())).thenReturn(accountEntityList);
        when(accountMapper.accountEntityToAccount(any())).thenReturn(accountList.get(0));
        when(responseValidator.applyValidations(any())).thenReturn(expectedResponse);

        AccountResponse actualResponse = accountService.getAccountsByUserId(any());

        verify(accountRepository, times(1)).findAllByUserId(any());
        assertEquals(expectedSize, actualResponse.getAccounts()
                .size());

        assertEquals(expectedResponse.getAccounts()
                .get(0)
                .getAccountNumber(), actualResponse.getAccounts()
                .get(0)
                .getAccountNumber());
        assertEquals(expectedResponse.getAccounts()
                .get(0)
                .getAccountName(), actualResponse.getAccounts()
                .get(0)
                .getAccountName());
        assertEquals(expectedResponse.getAccounts()
                .get(0)
                .getAccountType(), actualResponse.getAccounts()
                .get(0)
                .getAccountType());
        assertEquals(expectedResponse.getAccounts()
                .get(0)
                .getBalanceDate(), actualResponse.getAccounts()
                .get(0)
                .getBalanceDate());
        assertEquals(expectedResponse.getAccounts()
                .get(0)
                .getCurrency(), actualResponse.getAccounts()
                .get(0)
                .getCurrency());
        assertEquals(expectedResponse.getAccounts()
                .get(0)
                .getOpeningAvailableBalance(), actualResponse.getAccounts()
                .get(0)
                .getOpeningAvailableBalance());
    }

    @Test
    void givenInvalidUserId_getAccountsByUserId_throwHttpNotFoundError() {
        when(accountRepository.findAllByUserId(any())).thenReturn(List.of());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> accountService.getAccountsByUserId("123456"));
        assertEquals(NotFoundException.class, exception.getClass());
    }

    @Test
    void givenInvalidResponse_getAccountsByUserId_throwResponseValidationError() {
        when(accountRepository.findAllByUserId(any())).thenReturn(List.of(buildAccountEntity()));
        when(accountMapper.accountEntityToAccount(any())).thenReturn(buildAccount());
        when(responseValidator.applyValidations(any())).thenThrow(ResponseValidationException.class);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> accountService.getAccountsByUserId("123456"));
        assertEquals(ResponseValidationException.class, exception.getClass());
    }
}