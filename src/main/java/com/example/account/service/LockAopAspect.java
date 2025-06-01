package com.example.account.service;

import com.example.account.aop.AccountLockIdInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@RequiredArgsConstructor
@Component
public class LockAopAspect {

    private final RedisLockService lockService;

    @Around("@annotation(com.example.account.aop.AccountLock) && args(dto)")
    public Object aroundMethod(ProceedingJoinPoint joinPoint, AccountLockIdInterface dto) throws Throwable {
        // lock 취득 시도
        String accountNumber = dto.getAccountNumber();
        lockService.getLock(accountNumber);

        try {
            return joinPoint.proceed();
        } finally {
            // unlock
            lockService.unlock(accountNumber);
        }
    }
}

