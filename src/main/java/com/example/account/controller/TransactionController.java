package com.example.account.controller;

import com.example.account.aop.AccountLock;
import com.example.account.dto.transaction.CancelTransactionDto;
import com.example.account.dto.transaction.QueryTransactionDto;
import com.example.account.dto.transaction.UseTransactionDto;
import com.example.account.exception.AccountException;
import com.example.account.exception.TransactionException;
import com.example.account.service.AccountService;
import com.example.account.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final AccountService accountService;
    private final TransactionService transactionService;

    @PostMapping("/transaction/use")
    @AccountLock
    public UseTransactionDto.Response useBalance(
            @RequestBody @Valid UseTransactionDto.Request dto
    ) {
        UseTransactionDto.Response requestDto;

        try {
            requestDto = transactionService.useBalance(dto);
        } catch (AccountException | TransactionException e) {
            log.error("Transaction failed", e);

            transactionService.saveFailedTransaction(dto);

            throw e;
        }

        return requestDto;
    }

    @PostMapping("/transaction/cancel")
    @AccountLock
    public CancelTransactionDto.Response cancelTransaction(
            @RequestBody @Valid CancelTransactionDto.Request dto
    ) {
        CancelTransactionDto.Response responseDto = transactionService.cancelTransaction(dto);

        return responseDto;
    }

    @GetMapping("/transaction/{transactionId}")
    public QueryTransactionDto getTransaction(@PathVariable("transactionId") String id) {

        QueryTransactionDto responseDto = transactionService.findByTransactionId(id);

        return responseDto;
    }
}
