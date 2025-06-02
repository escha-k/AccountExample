package com.example.account.service;

import com.example.account.domain.Account;
import com.example.account.dto.account.AccountInfoDto;
import com.example.account.dto.account.DeleteAccountDto;
import com.example.account.type.AccountStatus;
import com.example.account.domain.AccountUser;
import com.example.account.dto.account.CreateAccountDto;
import com.example.account.exception.AccountException;
import com.example.account.repository.AccountRepository;
import com.example.account.repository.AccountUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.account.type.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountUserRepository accountUserRepository;

    private static final int MAX_ACCOUNT_PER_USER = 10;

    @Override
    @Transactional
    public CreateAccountDto.Response createAccount(CreateAccountDto.Request dto) {
        Long userId = dto.getUserId();
        Long initBalance = dto.getInitBalance();

        AccountUser user = getAccountUser(userId);
        validateAccountQuantity(user);

        Account saved = accountRepository.save(Account.builder()
                .accountUser(user)
                .accountNumber(generateAccountNumber())
                .accountStatus(AccountStatus.IN_USE)
                .balance(initBalance)
                .registeredAt(LocalDateTime.now())
                .build()
        );

        CreateAccountDto.Response responseDto = CreateAccountDto.Response.builder()
                .userId(saved.getAccountUser().getId())
                .accountNumber(saved.getAccountNumber())
                .registeredAt(saved.getRegisteredAt())
                .build();

        return responseDto;
    }

    private AccountUser getAccountUser(Long userId) {
        return accountUserRepository.findById(userId)
                .orElseThrow(() -> new AccountException(USER_NOT_FOUND));
    }

    private void validateAccountQuantity(AccountUser user) {
        // 사용자의 보유 계좌 수가 최대인 경우
        if (accountRepository.countByAccountUser(user) >= MAX_ACCOUNT_PER_USER) {
            throw new AccountException(MAX_ACCOUNT_PER_USER_OVER);
        }
    }

    private String generateAccountNumber() {
        String newAccountNumber = accountRepository.findFirstByOrderByIdDesc()
                .map(account -> Integer.toString(Integer.parseInt(account.getAccountNumber()) + 1))
                .orElse("1000000000");

        return newAccountNumber;
    }

    @Override
    public List<AccountInfoDto> getAccountsByUserId(Long userId) {
        AccountUser user = getAccountUser(userId);

        List<Account> accounts = accountRepository.findByAccountUser(user);
        List<AccountInfoDto> responseDtoList = accounts.stream()
                .filter(account -> account.getAccountStatus() == AccountStatus.IN_USE)
                .map(account -> AccountInfoDto.builder()
                        .accountNumber(account.getAccountNumber())
                        .balance(account.getBalance()).build())
                .toList();

        return responseDtoList;
    }

    @Override
    @Transactional
    public DeleteAccountDto.Response deleteAccount(DeleteAccountDto.Request requestDto) {
        Long userId = requestDto.getUserId();
        String accountNumber = requestDto.getAccountNumber();

        AccountUser user = getAccountUser(userId);
        Account deleteAccount = getAccount(accountNumber);
        validateDeleteAccount(user, deleteAccount);

        deleteAccount.setAccountStatus(AccountStatus.UNREGISTERED);
        deleteAccount.setUnRegisteredAt(LocalDateTime.now());

        DeleteAccountDto.Response responseDto = DeleteAccountDto.Response.builder()
                .userId(deleteAccount.getAccountUser().getId())
                .accountNumber(deleteAccount.getAccountNumber())
                .unRegisteredAt(deleteAccount.getUnRegisteredAt())
                .build();

        return responseDto;
    }

    private Account getAccount(String accountNumber) {
        Account deleteAccount = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountException(ACCOUNT_NOT_FOUND));
        return deleteAccount;
    }

    private void validateDeleteAccount(AccountUser user, Account account) {
        // 사용자 아이디와 계좌 소유주가 다른 경우
        Long userId = user.getId();
        Long accountUserId = account.getAccountUser().getId();
        if (!userId.equals(accountUserId)) {
            throw new AccountException(ACCOUNT_USER_NOT_MATCH);
        }

        // 계좌가 이미 해지 상태인 경우
        if (account.getAccountStatus() == AccountStatus.UNREGISTERED) {
            throw new AccountException(ACCOUNT_ALREADY_UNREGISTERED);
        }

        // 잔액이 있는 경우
        if (account.getBalance() > 0) {
            throw new AccountException(ACCOUNT_BALANCE_REMAINS);
        }
    }
}
