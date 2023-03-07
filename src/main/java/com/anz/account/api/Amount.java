package com.anz.account.api;

import com.anz.account.validation.ValidCurrency;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Amount {
    @JsonIgnore
    @ValidCurrency
    private final String currency;

    private final BigDecimal value;

    private Amount(String currency, BigDecimal value) {
        this.currency = currency;
        this.value = value;
    }

    public static Amount of(String currency, BigDecimal value) {
        return new Amount(currency, value);
    }
}
