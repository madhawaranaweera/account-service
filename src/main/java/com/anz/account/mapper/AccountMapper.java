package com.anz.account.mapper;

import com.anz.account.entity.AccountEntity;
import com.anz.common.api.Account;
import com.anz.common.api.Amount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Named("mapOpeningAvailableBalance")
    default Amount mapOpeningAvailableBalance(AccountEntity account) {
        return Amount.of(account.getCurrency(), account.getOpeningAvailableBalance());
    }

    @Mapping(source = ".", target = "openingAvailableBalance", qualifiedByName = "mapOpeningAvailableBalance")
    @Mapping(target = "accountId", source = "accountEntity.id")
    Account accountEntityToAccount(AccountEntity accountEntity);
}

