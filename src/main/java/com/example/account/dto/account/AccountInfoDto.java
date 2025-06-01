package com.example.account.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AccountInfoDto {

    private String accountNumber;
    private Long balance;
}
