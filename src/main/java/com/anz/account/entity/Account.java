package com.anz.account.entity;

import com.anz.account.api.AccountType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "account")
public class Account {

    @Id
    @Column(name="account_number", length=9)
    private String accountNumber;

    @Column(name="account_name", length=60, nullable=false)
    private String accountName;

    @Column(name="account_type", length=16, nullable=false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(name="balance_date", nullable=false)
    private LocalDate balanceDate;

    @Column(name="currency", length=3, nullable=false)
    private String currency;

    @Column(name="opening_available_balance", length=32)
    private BigDecimal openingAvailableBalance;

    @Column(name="user_id", length=16)
    private String userId;
}
