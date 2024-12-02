package me.tiary.dummydata.repository.custom;

import me.tiary.dummydata.domain.Account;

import java.util.List;

public interface AccountCustomRepository {
    void saveBatch(final List<Account> accounts);
}