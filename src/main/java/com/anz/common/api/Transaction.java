package com.anz.common.api;

import com.anz.common.validation.ValidAccountNumber;
import com.anz.common.validation.ValidCurrency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @ValidAccountNumber
    private String accountNumber;

    @NotNull
    @Size(min = 1, max = 60)
    private String accountName;

    @NotNull
    private LocalDate valueDate;

    @ValidCurrency
    private String currency;

    @Valid
    private Amount debitAmount;

    @Valid
    private Amount creditAmount;

    @NotNull
    private CreditDebitIndicator creditDebitIndicator;

    @Size(max = 60)
    private String transactionNarrative;
}
