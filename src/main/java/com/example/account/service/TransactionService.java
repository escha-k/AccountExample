package com.example.account.service;

import com.example.account.dto.transaction.CancelTransactionDto;
import com.example.account.dto.transaction.QueryTransactionDto;
import com.example.account.dto.transaction.UseTransactionDto;
import jakarta.validation.Valid;

public interface TransactionService {
    UseTransactionDto.Response useBalance(UseTransactionDto.Request requestDto);

    QueryTransactionDto findByTransactionId(String transactionId);

    void saveFailedTransaction(UseTransactionDto.Request requestDto);

    CancelTransactionDto.Response cancelTransaction(CancelTransactionDto.Request requestDto);
}
