package me.tiary.dummydata.repository.custom;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.domain.TilTag;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TilTagCustomRepositoryImpl implements TilTagCustomRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void saveBatch(final List<TilTag> tilTags) {
        final String sql = "INSERT INTO til_tag (til_id, tag_id, uuid, created_date, last_modified_date) " +
                "VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, tilTags, tilTags.size(), (ps, tilTag) -> {
            ps.setLong(1, tilTag.getTil().getId());
            ps.setLong(2, tilTag.getTag().getId());
            ps.setString(3, UUID.randomUUID().toString());
            ps.setObject(4, LocalDateTime.now());
            ps.setObject(5, LocalDateTime.now());
        });
    }
}