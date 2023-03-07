package com.anz.account.controller;

import com.anz.account.api.AccountResponse;
import com.anz.account.api.ApiError;
import com.anz.account.api.TransactionResponse;
import com.anz.account.service.AccountService;
import com.anz.account.service.TransactionService;
import com.anz.account.validation.ValidUserId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class AccountController {
    private final AccountService accountService;
    private final TransactionService transactionService;

    @Operation(
            summary = "Account list",
            description = "Provide list of account details for requested user<br/> "
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Account list",
                    content = @Content(schema = @Schema(implementation = AccountResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "Account service encountered a request validation error",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500",
                    description = "Account service encountered an unexpected internal error",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))})
    @GetMapping(value = "/users/{user-id}/accounts")
    public ResponseEntity<AccountResponse> getAccountsByUserId(
            @PathVariable("user-id") @ValidUserId final String userId) {
        log.info("Account service received view accounts request from {} user", userId);
        return new ResponseEntity<>(accountService.getAccountsByUserId(userId), HttpStatus.OK);
    }

    @Operation(
            summary = "Account transaction list",
            description = "Provide list of account transactions for requested account<br/> "
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Account transaction list",
                    content = @Content(schema = @Schema(implementation = AccountResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "Account service encountered a request validation error",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500",
                    description = "Account service encountered an unexpected internal error",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))})
    @GetMapping(value = "/users/{user-id}/accounts/{account-id}/transactions")
    public ResponseEntity<TransactionResponse> getTransactionsByAccountId(
            @PathVariable("user-id") @ValidUserId final String userId,
            @PathVariable("account-id") final Long accountId,
            @RequestParam @Min(value = 0) final int page,
            @RequestParam @Min(value = 1) final int size) {

        log.info("Account service received view transactions request from {} user", userId);
        return new ResponseEntity<>(transactionService.getTransactionsByAccountId
                (accountId, userId, page, size), HttpStatus.OK);

    }
}
