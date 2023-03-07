package com.anz.account.mapper;

import com.anz.account.api.Amount;
import com.anz.account.api.CreditDebitIndicator;
import com.anz.account.api.TransactionDto;
import com.anz.account.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Named("mapCreditAmount")
    static Amount mapCreditAmount(Transaction transaction) {
        if (CreditDebitIndicator.CREDIT.equals(transaction.getCreditDebitIndicator())) {
            return Amount.of(transaction.getCurrency(), transaction.getAmount());
        }
        return Amount.of(transaction.getCurrency(), BigDecimal.ZERO);
    }

    @Named("mapDebitAmount")
    static Amount mapDebitAmount(Transaction transaction) {
        if (CreditDebitIndicator.DEBIT.equals(transaction.getCreditDebitIndicator())) {
            return Amount.of(transaction.getCurrency(), transaction.getAmount());
        }
        return Amount.of(transaction.getCurrency(), BigDecimal.ZERO);
    }

    @Mapping(source = ".", target = "creditAmount", qualifiedByName = "mapCreditAmount")
    @Mapping(source = ".", target = "debitAmount", qualifiedByName = "mapDebitAmount")
    @Mapping(target = "accountNumber", source = "transaction.account.accountNumber")
    @Mapping(target = "accountName", source = "transaction.account.accountName")
    TransactionDto transactionToTransactionDto(Transaction transaction);
}
