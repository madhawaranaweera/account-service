package com.anz.account.mapper;

import com.anz.account.api.AccountDto;
import com.anz.account.entity.Account;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    List<AccountDto> mapAccountDetailsToResponse(List<Account> accounts);
}
