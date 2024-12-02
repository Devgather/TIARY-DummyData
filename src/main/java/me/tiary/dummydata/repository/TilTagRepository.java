package me.tiary.dummydata.repository;

import me.tiary.dummydata.domain.TilTag;
import me.tiary.dummydata.domain.composite.TilTagId;
import me.tiary.dummydata.repository.custom.TilTagCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TilTagRepository extends JpaRepository<TilTag, TilTagId>, TilTagCustomRepository {
}