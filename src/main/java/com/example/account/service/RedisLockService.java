package com.example.account.service;

import com.example.account.exception.AccountException;
import com.example.account.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisLockService {

    private final RedissonClient redissonClient;

    public void getLock(String accountNumber) {
        RLock lock = redissonClient.getLock(getLockKey(accountNumber));

        try {
            boolean isLock = lock.tryLock(1, 5, TimeUnit.SECONDS);
            if (!isLock) {
                log.error("=== Lock acquisition failed ===");
                throw new AccountException(ErrorCode.ACCOUNT_LOCKED);
            }
        } catch (AccountException e) {
            throw e;
        } catch (Exception e) {
            log.error("Redis lock failed", e);
        }
    }

    public void unlock(String accountNumber) {
        redissonClient.getLock(getLockKey(accountNumber)).unlock();
    }

    private static String getLockKey(String accountNumber) {
        return "ACLK" + accountNumber;
    }
}
