package me.tiary.dummydata.service;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.domain.Account;
import me.tiary.dummydata.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final TransactionTemplate transactionTemplate;

    private final AccountRepository accountRepository;

    public void insertAccounts(final List<Account> accounts) {
        transactionTemplate.executeWithoutResult(status -> {
            try {
                accountRepository.saveAllAndFlush(accounts);
            } catch (final Exception ex) {
                status.setRollbackOnly();
                throw ex;
            }
        });
    }
}