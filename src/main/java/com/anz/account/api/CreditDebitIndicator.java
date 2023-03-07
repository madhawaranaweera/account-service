package com.anz.account.api;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CreditDebitIndicator {

    DEBIT("Debit"),
    CREDIT("Credit");

    private final String value;
}
