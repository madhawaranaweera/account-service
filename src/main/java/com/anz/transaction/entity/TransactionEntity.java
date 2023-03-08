package com.anz.transaction.entity;

import com.anz.account.entity.AccountEntity;
import com.anz.common.api.CreditDebitIndicator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "transactions")
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "transaction_id_seq_gen")
    @SequenceGenerator(name = "transaction_id_seq_gen", sequenceName = "transaction_id_seq", allocationSize = 1)
    @Column
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private AccountEntity account;

    @Column(name = "value_date", nullable = false)
    private LocalDate valueDate;

    @Column(name = "currency", length = 3, nullable = false)
    private String currency;

    @Column(name = "amount", length = 20, nullable = false)
    private BigDecimal amount;

    @Column(name = "cr_dr_indicator", length = 6, nullable = false)
    @Enumerated(EnumType.STRING)
    private CreditDebitIndicator creditDebitIndicator;

    @Column(name = "transaction_narrative", length = 60)
    private String transactionNarrative;
}
