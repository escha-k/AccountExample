package com.example.account.service;

import com.example.account.domain.Account;
import com.example.account.dto.account.AccountInfoDto;
import com.example.account.dto.account.CreateAccountDto;
import com.example.account.dto.account.DeleteAccountDto;

import java.util.List;

public interface AccountService {

    public CreateAccountDto.Response createAccount(CreateAccountDto.Request requestDto);

    public DeleteAccountDto.Response deleteAccount(DeleteAccountDto.Request requestDto);

    public List<AccountInfoDto> getAccountsByUserId(Long userId);

    public Account getAccount(Long id);
}
