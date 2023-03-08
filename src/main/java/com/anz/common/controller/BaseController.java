package com.anz.common.controller;

import com.anz.account.controller.AccountController;
import com.anz.common.api.Account;
import com.anz.common.api.TransactionResponse;
import com.anz.transaction.controller.TransactionController;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
public class BaseController {

    protected void applyTransactionHateoas(List<Account> accountDtos, String userId) {
        accountDtos.stream()
                .peek(accountDto -> accountDto.add(linkTo(methodOn(TransactionController.class)
                        .getTransactionsByAccountId(userId, accountDto.getAccountId(), 0, 50))
                        .withRel("transactions")))
                .collect(Collectors.toList());
    }

    protected void applyAccountHateoas(TransactionResponse transactionResponse, String userId) {
        transactionResponse.add(linkTo(methodOn(AccountController.class)
                .getAccountsByUserId(userId))
                .withRel("accounts"));
    }
}
