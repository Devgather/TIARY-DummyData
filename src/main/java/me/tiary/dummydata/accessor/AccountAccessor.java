package me.tiary.dummydata.accessor;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.domain.Account;
import me.tiary.dummydata.repository.AccountRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AccountAccessor {
    private final AccountRepository accountRepository;

    public void insertAccounts(final List<Account> accounts) {
        accountRepository.saveBatch(accounts);
    }
}