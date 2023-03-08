package com.anz.transaction.mapper;

import com.anz.common.api.Amount;
import com.anz.common.api.CreditDebitIndicator;
import com.anz.common.api.Transaction;
import com.anz.transaction.entity.TransactionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Named("mapCreditAmount")
    default Amount mapCreditAmount(TransactionEntity transaction) {
        if (CreditDebitIndicator.CREDIT.equals(transaction.getCreditDebitIndicator())) {
            return Amount.of(transaction.getCurrency(), transaction.getAmount());
        }
        return Amount.of(transaction.getCurrency(), BigDecimal.ZERO);
    }

    @Named("mapDebitAmount")
    default Amount mapDebitAmount(TransactionEntity transaction) {
        if (CreditDebitIndicator.DEBIT.equals(transaction.getCreditDebitIndicator())) {
            return Amount.of(transaction.getCurrency(), transaction.getAmount());
        }
        return Amount.of(transaction.getCurrency(), BigDecimal.ZERO);
    }

    @Mapping(source = ".", target = "creditAmount", qualifiedByName = "mapCreditAmount")
    @Mapping(source = ".", target = "debitAmount", qualifiedByName = "mapDebitAmount")
    @Mapping(target = "accountNumber", source = "transactionEntity.account.accountNumber")
    @Mapping(target = "accountName", source = "transactionEntity.account.accountName")
    Transaction transactionEntityToTransaction(TransactionEntity transactionEntity);
}
