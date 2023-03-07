package com.anz.account.api;

import com.anz.account.validation.ValidAccountNumber;
import com.anz.account.validation.ValidCurrency;
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
public class TransactionDto {
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
