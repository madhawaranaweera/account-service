package com.anz.account.service;

import com.anz.account.api.AccountDto;
import com.anz.account.api.AccountType;
import com.anz.account.api.ViewAccountsResponse;
import com.anz.account.entity.Account;
import com.anz.account.mapper.AccountMapper;
import com.anz.account.repository.AccountRepository;
import com.anz.account.validation.ResponseValidator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @ParameterizedTest()
    @MethodSource
    public void getAccountsByUserId_success(List<Account> accountList, int expectedSize, List<AccountDto> accountDtoList) {
        ViewAccountsResponse expectedResponse = ViewAccountsResponse.builder().accounts(accountDtoList).build();

        when(accountRepository.findAllByUserId(any())).thenReturn(accountList);
        when(accountMapper.mapAccountDetailsToResponse(any())).thenReturn(accountDtoList);
        when(responseValidator.applyValidations(any())).thenReturn(expectedResponse);

        ViewAccountsResponse actualResponse = accountService.getAccountsByUserId(any());

        verify(accountRepository,times(1)).findAllByUserId(any());
        assertEquals(expectedSize,actualResponse.getAccounts().size());

        if(expectedSize != 0) {
            assertEquals(expectedResponse.getAccounts().get(0).getAccountNumber(),actualResponse.getAccounts().get(0).getAccountNumber());
            assertEquals(expectedResponse.getAccounts().get(0).getAccountName(),actualResponse.getAccounts().get(0).getAccountName());
            assertEquals(expectedResponse.getAccounts().get(0).getAccountType(),actualResponse.getAccounts().get(0).getAccountType());
            assertEquals(expectedResponse.getAccounts().get(0).getBalanceDate(),actualResponse.getAccounts().get(0).getBalanceDate());
            assertEquals(expectedResponse.getAccounts().get(0).getCurrency(),actualResponse.getAccounts().get(0).getCurrency());
            assertEquals(expectedResponse.getAccounts().get(0).getOpeningAvailableBalance(),actualResponse.getAccounts().get(0).getOpeningAvailableBalance());
        }
    }

    private static Stream<Arguments> getAccountsByUserId_success() {
        return Stream.of(
                Arguments.of(List.of(buildAccount()), 1, List.of(buildAccountDto())),
                Arguments.of(List.of(buildAccount(),buildAccount()), 2, List.of(buildAccountDto(),buildAccountDto())),
                Arguments.of(new ArrayList<Account>(), 0, new ArrayList<AccountDto>())
        );
    }

    private static Account buildAccount() {
        return Account.builder().accountNumber("800200700").accountName("Madhawa").accountType(AccountType.Current)
                .userId("123456").balanceDate(LocalDate.of(2023,01,01)).currency("AUD")
                .openingAvailableBalance(BigDecimal.valueOf(1001.50)).build();
    }

    private static AccountDto buildAccountDto() {
        return AccountDto.builder().accountNumber("800200700").accountName("Madhawa").accountType(AccountType.Current)
                .balanceDate(LocalDate.of(2023,01,01)).currency("AUD")
                .openingAvailableBalance(BigDecimal.valueOf(1001.50)).build();
    }
}