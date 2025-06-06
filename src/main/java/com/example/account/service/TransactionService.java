package com.example.account.service;

import com.example.account.domain.Account;
import com.example.account.domain.AccountUser;
import com.example.account.domain.Transaction;
import com.example.account.dto.transaction.CancelTransactionDto;
import com.example.account.dto.transaction.QueryTransactionDto;
import com.example.account.dto.transaction.UseTransactionDto;
import com.example.account.exception.AccountException;
import com.example.account.exception.TransactionException;
import com.example.account.repository.AccountRepository;
import com.example.account.repository.AccountUserRepository;
import com.example.account.repository.TransactionRepository;
import com.example.account.type.AccountStatus;
import com.example.account.type.TransactionResultType;
import com.example.account.type.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.example.account.type.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class TransactionService {

    private final AccountRepository accountRepository;
    private final AccountUserRepository accountUserRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public UseTransactionDto.Response useBalance(UseTransactionDto.Request requestDto) {
        Long userId = requestDto.getUserId();
        String accountNumber = requestDto.getAccountNumber();
        Long amount = requestDto.getAmount();

        // 사용자, 계좌 불러오기 및 검증
        AccountUser user = getAccountUser(userId);
        Account account = getAccount(accountNumber);
        validateUseTransaction(user, account, amount);

        // 결제 로직
        account.useBalance(amount);
        Transaction saved = saveTransaction(TransactionType.USE, TransactionResultType.S, account, amount);

        // 응답 생성 및 반환
        UseTransactionDto.Response responseDto = UseTransactionDto.Response.builder()
                .accountNumber(saved.getAccount().getAccountNumber())
                .transactionResult(saved.getTransactionResultType())
                .transactionId(saved.getTransactionId())
                .amount(saved.getAmount())
                .transactedAt(saved.getTransactedAt())
                .build();

        return responseDto;
    }

    public CancelTransactionDto.Response cancelTransaction(CancelTransactionDto.Request requestDto) {
        String transactionId = requestDto.getTransactionId();
        String accountNumber = requestDto.getAccountNumber();
        Long amount = requestDto.getAmount();

        // 트랜잭션 불러오기 및 검증
        Transaction transaction = getTransactionByTransactionId(transactionId);
        Account account = getAccount(accountNumber);
        validateCancelTransaction(transaction, account, amount);

        // 결제 취소 로직
        account.cancelBalance(transaction.getAmount());
        Transaction saved = saveTransaction(TransactionType.CANCEL, TransactionResultType.S, account, amount);

        // 응답 생성 및 반환
        CancelTransactionDto.Response responseDto = CancelTransactionDto.Response.builder()
                .accountNumber(saved.getAccount().getAccountNumber())
                .transactionResult(saved.getTransactionResultType())
                .transactionId(saved.getTransactionId())
                .amount(saved.getAmount())
                .transactedAt(saved.getTransactedAt())
                .build();

        return responseDto;

    }

    public QueryTransactionDto findByTransactionId(String transactionId) {
        // 트랜잭션 불러오기 및 검증
        Transaction transaction = getTransactionByTransactionId(transactionId);

        // 응답 생성 및 반환
        QueryTransactionDto responseDto = QueryTransactionDto.builder()
                .accountNumber(transaction.getAccount().getAccountNumber())
                .transactionType(transaction.getTransactionType())
                .transactionResult(transaction.getTransactionResultType())
                .transactionId(transaction.getTransactionId())
                .amount(transaction.getAmount())
                .transactedAt(transaction.getTransactedAt())
                .build();

        return responseDto;
    }

    public void saveFailedTransaction(UseTransactionDto.Request requestDto) {
        String accountNumber = requestDto.getAccountNumber();
        Long amount = requestDto.getAmount();

        Account account = getAccount(accountNumber);

        saveTransaction(TransactionType.USE, TransactionResultType.F, account, amount);
    }

    private Transaction saveTransaction(TransactionType type, TransactionResultType result, Account account, Long amount) {
        Transaction saved = transactionRepository.save(Transaction.builder()
                .transactionType(type)
                .transactionResultType(result)
                .account(account)
                .amount(amount)
                .balanceSnapshot(account.getBalance())
                .transactionId(UUID.randomUUID().toString())
                .transactedAt(LocalDateTime.now())
                .build()
        );
        return saved;
    }

    private Account getAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountException(ACCOUNT_NOT_FOUND));
        return account;
    }

    private AccountUser getAccountUser(Long userId) {
        AccountUser user = accountUserRepository.findById(userId)
                .orElseThrow(() -> new AccountException(USER_NOT_FOUND));
        return user;
    }

    private Transaction getTransactionByTransactionId(String transactionId) {
        Transaction transaction = transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new TransactionException(TRANSACTION_NOT_FOUND));
        return transaction;
    }

    private void validateUseTransaction(AccountUser user, Account account, Long amount) {
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

        // 거래금액이 잔액보다 큰 경우
        Long balance = account.getBalance();
        if (balance < amount) {
            throw new AccountException(AMOUNT_OVER_BALANCE);
        }
    }

    private void validateCancelTransaction(Transaction transaction, Account account, Long amount) {
        // 거래와 계좌가 일치하지 않는 경우
        Long transactionAccountId = transaction.getAccount().getId();
        if (!transactionAccountId.equals(account.getId())) {
            throw new TransactionException(TRANSACTION_ACCOUNT_NOT_MATCH);
        }

        // 거래 금액과 거래 취소 금액이 다른 경우 (부분 취소 불가)
        if (!transaction.getAmount().equals(amount)) {
            throw new TransactionException(TRANSACTION_PARTIAL_CANCEL_NOT_ALLOWED);
        }

        // 1년이 넘은 거래는 취소 불가능
        if (transaction.getTransactedAt().isBefore(LocalDateTime.now().minusYears(1))) {
            throw new TransactionException(TRANSACTION_TOO_OLD_TO_CANCEL);
        }
    }
}
