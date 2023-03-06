package com.anz.account.api;

import com.anz.account.validation.ValidAccountNumber;
import com.anz.account.validation.ValidCurrency;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    @ValidAccountNumber
    private String accountNumber;

    @NotNull
    @Size(min = 1, max = 60)
    private String accountName;

    @NotNull
    private AccountType accountType;

    @NotNull
    private LocalDate balanceDate;

    @ValidCurrency
    private String currency;

    private BigDecimal openingAvailableBalance;
}
