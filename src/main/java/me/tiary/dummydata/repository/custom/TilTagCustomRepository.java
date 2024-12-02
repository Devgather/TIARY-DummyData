package me.tiary.dummydata.repository.custom;

import me.tiary.dummydata.domain.TilTag;

import java.util.List;

public interface TilTagCustomRepository {
    void saveBatch(final List<TilTag> tilTags);
}