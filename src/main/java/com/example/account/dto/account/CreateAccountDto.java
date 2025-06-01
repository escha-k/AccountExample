package com.example.account.dto.account;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class CreateAccountDto {

    @Getter
    @Setter
    public static class Request {

        @NotNull
        @Min(1)
        private Long userId;

        @NotNull
        @Min(0)
        private Long initBalance;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Response {

        private Long userId;
        private String accountNumber;
        private LocalDateTime registeredAt;
    }
}
