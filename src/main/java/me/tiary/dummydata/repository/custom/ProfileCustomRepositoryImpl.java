package me.tiary.dummydata.repository.custom;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.domain.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProfileCustomRepositoryImpl implements ProfileCustomRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void saveBatch(final List<Profile> profiles) {
        final String sql = "INSERT INTO profile (uuid, nickname, picture, created_date, last_modified_date) " +
                "VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, profiles, profiles.size(), (ps, profile) -> {
            ps.setString(1, UUID.randomUUID().toString());
            ps.setString(2, profile.getNickname());
            ps.setString(3, profile.getPicture());
            ps.setObject(4, LocalDateTime.now());
            ps.setObject(5, LocalDateTime.now());
        });
    }
}