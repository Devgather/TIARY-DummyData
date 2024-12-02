package me.tiary.dummydata.repository.custom;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.domain.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AccountCustomRepositoryImpl implements AccountCustomRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void saveBatch(final List<Account> accounts) {
        final String sql = "INSERT INTO account (profile_id, uuid, email, password, created_date, last_modified_date) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, accounts, accounts.size(), (ps, account) -> {
            ps.setLong(1, account.getProfile().getId());
            ps.setString(2, UUID.randomUUID().toString());
            ps.setString(3, account.getEmail());
            ps.setString(4, account.getPassword());
            ps.setObject(5, LocalDateTime.now());
            ps.setObject(6, LocalDateTime.now());
        });
    }
}