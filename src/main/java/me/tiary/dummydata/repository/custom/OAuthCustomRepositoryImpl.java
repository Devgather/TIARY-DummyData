package me.tiary.dummydata.repository.custom;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.domain.OAuth;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OAuthCustomRepositoryImpl implements OAuthCustomRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void saveBatch(final List<OAuth> oAuths) {
        final String sql = "INSERT INTO oauth (profile_id, uuid, identifier, provider, created_date, last_modified_date) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, oAuths, oAuths.size(), (ps, oAuth) -> {
            ps.setLong(1, oAuth.getProfile().getId());
            ps.setString(2, UUID.randomUUID().toString());
            ps.setString(3, oAuth.getIdentifier());
            ps.setString(4, oAuth.getProvider());
            ps.setObject(5, LocalDateTime.now());
            ps.setObject(6, LocalDateTime.now());
        });
    }
}