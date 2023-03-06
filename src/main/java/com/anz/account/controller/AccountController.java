package com.anz.account.controller;

import com.anz.account.api.ViewAccountsResponse;
import com.anz.account.service.AccountService;
import com.anz.account.validation.ValidUserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class AccountController {
    private final AccountService accountService;

    @GetMapping(value = "/users/{userId}/accounts")
    public ResponseEntity<ViewAccountsResponse> getAccountsByUserId(
            @PathVariable @ValidUserId final String userId) {
        log.info("Account service received view accounts request for {} user", userId);
        return new ResponseEntity<>(accountService.getAccountsByUserId(userId), HttpStatus.OK);
    }
}
