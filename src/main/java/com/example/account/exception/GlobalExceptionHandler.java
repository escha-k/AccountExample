package com.example.account.exception;

import com.example.account.dto.ErrorResponseDto;
import com.example.account.type.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.example.account.type.ErrorCode.INTERNAL_SERVER_ERROR;
import static com.example.account.type.ErrorCode.INVALID_REQUEST;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountException.class)
    public ErrorResponseDto handleAccountException(AccountException e) {
        log.error("{} occurred", e.getMessage());

        return new ErrorResponseDto(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(TransactionException.class)
    public ErrorResponseDto handleTransactionException(TransactionException e) {
        log.error("{} occurred", e.getMessage());

        return new ErrorResponseDto(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorResponseDto handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("DataIntegrityViolationException occurred", e);

        return new ErrorResponseDto(INVALID_REQUEST, INVALID_REQUEST.getDescription());
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponseDto handleException(Exception e) {
        log.error("Exception occurred", e);

        return new ErrorResponseDto(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR.getDescription());
    }
}
