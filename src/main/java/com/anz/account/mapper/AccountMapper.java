package com.anz.account.mapper;

import com.anz.account.api.AccountDto;
import com.anz.account.api.Amount;
import com.anz.account.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Named("mapOpeningAvailableBalance")
    static Amount mapOpeningAvailableBalance(Account account) {
        return Amount.of(account.getCurrency(), account.getOpeningAvailableBalance());
    }

    @Mapping(source = ".", target = "openingAvailableBalance", qualifiedByName = "mapOpeningAvailableBalance")
    @Mapping(target = "accountId", source = "account.id")
    AccountDto accountToAccountDto(Account account);
}

