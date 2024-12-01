package me.tiary.dummydata.repository.custom;

import me.tiary.dummydata.domain.Profile;

import java.util.List;

public interface ProfileCustomRepository {
    void saveBatch(final List<Profile> profiles);
}