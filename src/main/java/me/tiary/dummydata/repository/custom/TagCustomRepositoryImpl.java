package me.tiary.dummydata.repository.custom;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.domain.Tag;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TagCustomRepositoryImpl implements TagCustomRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void saveBatch(final List<Tag> tags) {
        final String sql = "INSERT INTO tag (uuid, name, created_date, last_modified_date) " +
                "VALUES (?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, tags, tags.size(), (ps, tag) -> {
            ps.setString(1, UUID.randomUUID().toString());
            ps.setString(2, tag.getName());
            ps.setObject(3, LocalDateTime.now());
            ps.setObject(4, LocalDateTime.now());
        });
    }
}