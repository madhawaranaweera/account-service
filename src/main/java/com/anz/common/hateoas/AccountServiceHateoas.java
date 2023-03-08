package com.anz.common.hateoas;

import com.anz.account.controller.AccountController;
import com.anz.common.api.Account;
import com.anz.common.api.TransactionResponse;
import com.anz.transaction.controller.TransactionController;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AccountServiceHateoas {
    public void applyTransactionHateoas(List<Account> accounts, String userId) {
        accounts.stream()
                .peek(account -> account.add(linkTo(methodOn(TransactionController.class)
                        .getTransactionsByAccountId(userId, account.getAccountId(), 0, 50))
                        .withRel("transactions")))
                .collect(Collectors.toList());
    }

    public void applyAccountHateoas(TransactionResponse transactionResponse, String userId) {
        transactionResponse.add(linkTo(methodOn(AccountController.class)
                .getAccountsByUserId(userId))
                .withRel("accounts"));
    }
}
