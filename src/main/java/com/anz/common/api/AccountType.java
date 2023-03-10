package com.anz.common.api;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AccountType {
    Savings("Savings"),
    Current("Current");

    private final String value;

    public String getValue() {
        return value;
    }
}
