package com.example.account.dto.transaction;

import com.example.account.type.TransactionResultType;
import com.example.account.type.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class QueryTransactionDto {

    private String accountNumber;
    private TransactionType transactionType;
    private TransactionResultType transactionResult;
    private String transactionId;
    private Long amount;
    private LocalDateTime transactedAt;
}
