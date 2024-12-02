package me.tiary.dummydata.repository.custom;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.domain.Til;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TilCustomRepositoryImpl implements TilCustomRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void saveBatch(final List<Til> tils) {
        final String sql = "INSERT INTO til (profile_id, uuid, title, content, created_date, last_modified_date) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, tils, tils.size(), (ps, til) -> {
            ps.setLong(1, til.getProfile().getId());
            ps.setString(2, UUID.randomUUID().toString());
            ps.setString(3, til.getTitle());
            ps.setString(4, til.getContent());
            ps.setObject(5, LocalDateTime.now());
            ps.setObject(6, LocalDateTime.now());
        });
    }
}