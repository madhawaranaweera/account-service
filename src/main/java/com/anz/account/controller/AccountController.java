package com.anz.account.controller;

import com.anz.account.service.AccountService;
import com.anz.common.api.AccountResponse;
import com.anz.common.api.ApiError;
import com.anz.common.hateoas.AccountServiceHateoas;
import com.anz.common.validation.ValidUserId;
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
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class AccountController {
    private final AccountService accountService;
    private final AccountServiceHateoas accountServiceHateoas;

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
            @ApiResponse(responseCode = "404",
                    description = "Account service is unable find requested resource",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500",
                    description = "Account service encountered an unexpected internal error",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))})

    @GetMapping(value = "/users/{user-id}/accounts")
    public ResponseEntity<AccountResponse> getAccountsByUserId(
            @PathVariable("user-id") @ValidUserId final String userId) {

        log.info("Account service received view accounts request");

        AccountResponse accountResponse = accountService.getAccountsByUserId(userId);
        accountServiceHateoas.applyTransactionHateoas(accountResponse.getAccounts(), userId);
        return new ResponseEntity<>(accountResponse, HttpStatus.OK);
    }
}
