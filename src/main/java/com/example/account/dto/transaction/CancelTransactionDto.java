package com.example.account.dto.transaction;

import com.example.account.aop.AccountLockIdInterface;
import com.example.account.type.TransactionResultType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class CancelTransactionDto {

    @Getter
    @Setter
    public static class Request implements AccountLockIdInterface {

        @NotNull
        private String transactionId;

        @NotBlank
        @Size(min = 10, max = 10)
        private String accountNumber;

        @NotNull
        private Long amount;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Response {

        private String accountNumber;
        private TransactionResultType transactionResult;
        private String transactionId;
        private Long amount;
        private LocalDateTime transactedAt;
    }

}
