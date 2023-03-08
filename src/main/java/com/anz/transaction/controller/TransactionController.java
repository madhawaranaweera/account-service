package com.anz.transaction.controller;

import com.anz.common.api.AccountResponse;
import com.anz.common.api.ApiError;
import com.anz.common.api.TransactionResponse;
import com.anz.common.hateoas.AccountServiceHateoas;
import com.anz.common.validation.ValidUserId;
import com.anz.transaction.service.TransactionService;
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
public class TransactionController {
    private final TransactionService transactionService;
    private final AccountServiceHateoas accountServiceHateoas;

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
            @ApiResponse(responseCode = "404",
                    description = "Account service is unable find requested resource",
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

        log.info("Account service received view transactions request");

        TransactionResponse transactionResponse = transactionService.getTransactionsByAccountId(accountId, userId, page, size);
        accountServiceHateoas.applyAccountHateoas(transactionResponse, userId);
        return new ResponseEntity<>(transactionResponse, HttpStatus.OK);

    }
}
