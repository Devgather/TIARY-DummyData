package me.tiary.dummydata.repository;

import me.tiary.dummydata.domain.Account;
import me.tiary.dummydata.repository.custom.AccountCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>, AccountCustomRepository {
}