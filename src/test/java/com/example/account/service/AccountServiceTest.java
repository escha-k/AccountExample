package com.example.account.service;

import com.example.account.domain.Account;
import com.example.account.domain.AccountUser;
import com.example.account.dto.account.CreateAccountDto;
import com.example.account.repository.AccountRepository;
import com.example.account.repository.AccountUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    AccountRepository accountRepository;
    @Mock
    AccountUserRepository accountUserRepository;
    @InjectMocks
    AccountService accountService;

    @Test
    void createAccount() {
        AccountUser user = AccountUser.builder().id(10L).name("user").build();
        Account account = Account.builder()
                .accountUser(user)
                .accountNumber("1000000000")
                .build();
        given(accountUserRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(accountRepository.findFirstByOrderByIdDesc()).willReturn(Optional.of(account));
        given(accountRepository.save(any(Account.class))).willReturn(Account.builder()
                .accountUser(user)
                .accountNumber("1000000001")
                .build()
        );

        CreateAccountDto.Request dto = new CreateAccountDto.Request();
        dto.setUserId(10L);
        dto.setInitBalance(1000L);
        CreateAccountDto.Response responseDto = accountService.createAccount(dto);

        assertEquals(10L, responseDto.getUserId());
        assertEquals("1000000001", responseDto.getAccountNumber());

    }

    @Test
    void deleteAccount() {
    }

    @Test
    void getAccountsByUserId() {
    }
}