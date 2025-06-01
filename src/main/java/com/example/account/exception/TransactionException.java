package com.example.account.exception;

import com.example.account.type.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionException extends RuntimeException {
    private ErrorCode errorCode;
    private String errorMessage;

    public TransactionException(ErrorCode errorCode) {
      this.errorCode = errorCode;
      this.errorMessage = errorCode.getDescription();
    }
}
