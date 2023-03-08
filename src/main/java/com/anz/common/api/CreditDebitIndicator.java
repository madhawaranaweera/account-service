package com.anz.common.api;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CreditDebitIndicator {
    DEBIT("Debit"),
    CREDIT("Credit");

    private final String value;
}
