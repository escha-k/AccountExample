package com.example.account.controller;

import com.example.account.domain.Account;
import com.example.account.domain.AccountUser;
import com.example.account.dto.account.AccountInfoDto;
import com.example.account.dto.account.CreateAccountDto;
import com.example.account.dto.account.DeleteAccountDto;
import com.example.account.repository.AccountRepository;
import com.example.account.repository.AccountUserRepository;
import com.example.account.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AccountController.class)
class AccountControllerTest {

    @MockitoBean
    AccountService accountService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    @DisplayName("CreateAccount - 정상 로직")
    void CreateAccountTestSuccess() throws Exception {
        given(accountService.createAccount(any(CreateAccountDto.Request.class))).willReturn(
                CreateAccountDto.Response.builder()
                        .userId(1L)
                        .accountNumber("1000000000")
                        .registeredAt(LocalDateTime.now())
                        .build()
        );

        CreateAccountDto.Request request = new CreateAccountDto.Request();
        request.setUserId(1L);
        request.setInitBalance(1000L);

        mockMvc.perform(post("/account").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.accountNumber").value("1000000000"))
                .andDo(print());
    }

    @Test
    @DisplayName("DeleteAccount - 정상 로직")
    void DeleteAccountTestSuccess() throws Exception {
        given(accountService.deleteAccount(any(DeleteAccountDto.Request.class))).willReturn(
                DeleteAccountDto.Response.builder()
                        .userId(1L)
                        .accountNumber("1000000000")
                        .unRegisteredAt(LocalDateTime.now())
                        .build()
        );

        DeleteAccountDto.Request request = new DeleteAccountDto.Request();
        request.setUserId(1L);
        request.setAccountNumber("1000000000");

        mockMvc.perform(delete("/account").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.accountNumber").value("1000000000"))
                .andDo(print());
    }

    @Test
    @DisplayName("GetAccount - 정상 로직")
    void GetAccountTestSuccess() throws Exception {
        List<AccountInfoDto> accountInfoDtos = Arrays.asList(
                AccountInfoDto.builder()
                        .accountNumber("1000000000")
                        .balance(1000L)
                        .build(),
                AccountInfoDto.builder()
                        .accountNumber("1000000001")
                        .balance(2000L)
                        .build(),
                AccountInfoDto.builder()
                        .accountNumber("1000000002")
                        .balance(3000L)
                        .build()
        );
        given(accountService.getAccountsByUserId(anyLong())).willReturn(accountInfoDtos);

        mockMvc.perform(get("/account?user_id=1"))
                .andDo(print())
                .andExpect(jsonPath("$[0].accountNumber").value("1000000000"))
                .andExpect(jsonPath("$[0].balance").value(1000L))
                .andExpect(jsonPath("$[1].accountNumber").value("1000000001"))
                .andExpect(jsonPath("$[1].balance").value(2000L))
                .andExpect(jsonPath("$[2].accountNumber").value("1000000002"))
                .andExpect(jsonPath("$[2].balance").value(3000L));
    }
}