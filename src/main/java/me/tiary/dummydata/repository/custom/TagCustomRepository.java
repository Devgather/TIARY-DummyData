package me.tiary.dummydata.repository.custom;

import me.tiary.dummydata.domain.Tag;

import java.util.List;

public interface TagCustomRepository {
    void saveBatch(final List<Tag> tags);
}