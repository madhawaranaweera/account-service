package com.anz.account.service;

import com.anz.account.api.AccountDto;
import com.anz.account.api.ViewAccountsResponse;
import com.anz.account.mapper.AccountMapper;
import com.anz.account.repository.AccountRepository;
import com.anz.account.validation.ResponseValidator;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Builder
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final ResponseValidator responseValidator;

    public ViewAccountsResponse getAccountsByUserId(String userId){
        List<AccountDto> accounts = accountMapper.mapAccountDetailsToResponse(accountRepository.findAllByUserId(userId));
        log.debug("Account service retrieved {} accounts for user {} ", accounts.size(), userId);
        ViewAccountsResponse viewAccountsResponse = ViewAccountsResponse.builder().accounts(accounts).build();
        responseValidator.applyValidations(viewAccountsResponse);
        return viewAccountsResponse;
    }
}
