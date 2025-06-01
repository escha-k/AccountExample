package com.example.account.dto.account;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class DeleteAccountDto {

    @Getter
    @Setter
    public static class Request {

        @NotNull
        @Min(1)
        private Long userId;

        @NotBlank
        @Size(min = 10, max = 10)
        private String accountNumber;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Response {

        private Long userId;
        private String accountNumber;
        private LocalDateTime unRegisteredAt;
    }
}
