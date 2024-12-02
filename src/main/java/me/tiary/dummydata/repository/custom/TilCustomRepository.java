package me.tiary.dummydata.repository.custom;

import me.tiary.dummydata.domain.Til;

import java.util.List;

public interface TilCustomRepository {
    void saveBatch(final List<Til> tils);
}