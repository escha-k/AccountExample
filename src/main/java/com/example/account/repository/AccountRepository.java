package com.example.account.repository;

import com.example.account.domain.Account;
import com.example.account.domain.AccountUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findFirstByOrderByIdDesc();
    Optional<Account> findByAccountNumber(String accountNumber);

    Integer countByAccountUser(AccountUser user);

    List<Account> findByAccountUser(AccountUser user);
}
