package me.tiary.dummydata.repository.custom;

import me.tiary.dummydata.domain.OAuth;

import java.util.List;

public interface OAuthCustomRepository {
    void saveBatch(final List<OAuth> oAuths);
}