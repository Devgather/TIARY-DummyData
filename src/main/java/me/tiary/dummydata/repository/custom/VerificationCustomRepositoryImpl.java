package me.tiary.dummydata.repository.custom;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.domain.Verification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class VerificationCustomRepositoryImpl implements VerificationCustomRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void saveBatch(final List<Verification> verifications) {
        final String sql = "INSERT INTO verification (uuid, email, code, state, created_date, last_modified_date) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, verifications, verifications.size(), (ps, verification) -> {
            ps.setString(1, UUID.randomUUID().toString());
            ps.setString(2, verification.getEmail());
            ps.setString(3, verification.getCode());
            ps.setBoolean(4, verification.getState());
            ps.setObject(5, LocalDateTime.now());
            ps.setObject(6, LocalDateTime.now());
        });
    }
}