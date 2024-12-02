package me.tiary.dummydata.repository.custom;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.domain.Comment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CommentCustomRepositoryImpl implements CommentCustomRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void saveBatch(final List<Comment> comments) {
        final String sql = "INSERT INTO comment (profile_id, til_id, uuid, content, created_date, last_modified_date) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, comments, comments.size(), (ps, comment) -> {
            ps.setLong(1, comment.getProfile().getId());
            ps.setLong(2, comment.getTil().getId());
            ps.setString(3, UUID.randomUUID().toString());
            ps.setString(4, comment.getContent());
            ps.setObject(5, LocalDateTime.now());
            ps.setObject(6, LocalDateTime.now());
        });
    }
}