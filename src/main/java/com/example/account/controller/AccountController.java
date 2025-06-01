package com.example.account.controller;

import com.example.account.dto.account.CreateAccountDto;
import com.example.account.dto.account.AccountInfoDto;
import com.example.account.dto.account.DeleteAccountDto;
import com.example.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/account")
    public List<AccountInfoDto> getAccount(@RequestParam("user_id") Long id) {
        List<AccountInfoDto> responseDto = accountService.getAccountsByUserId(id);

        return responseDto;
    }

    @PostMapping("/account")
    public CreateAccountDto.Response createAccount(
            @RequestBody @Valid CreateAccountDto.Request dto
    ) {
        CreateAccountDto.Response responseDto = accountService.createAccount(dto);

        return responseDto;
    }

    @DeleteMapping("/account")
    public DeleteAccountDto.Response deleteAccount(
            @RequestBody @Valid DeleteAccountDto.Request dto
    ) {
        DeleteAccountDto.Response responseDto = accountService.deleteAccount(dto);

        return responseDto;
    }
}
