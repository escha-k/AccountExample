package com.example.account.dto;

import com.example.account.type.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponseDto {

    private ErrorCode errorCode;
    private String errorMessage;
}
