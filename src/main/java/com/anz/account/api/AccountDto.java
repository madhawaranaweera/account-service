package com.anz.account.api;

import com.anz.account.validation.ValidAccountNumber;
import com.anz.account.validation.ValidCurrency;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto extends RepresentationModel<AccountDto> {
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

    @Valid
    private Amount openingAvailableBalance;

    @JsonIgnore
    private Long accountId;

}
