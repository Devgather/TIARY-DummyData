package me.tiary.dummydata.repository;

import me.tiary.dummydata.domain.Til;
import me.tiary.dummydata.repository.custom.TilCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TilRepository extends JpaRepository<Til, Long>, TilCustomRepository {
    Optional<Til> findFirstByOrderByIdAsc();

    Optional<Til> findFirstByOrderByIdDesc();

    List<Til> findAllByIdBetween(final Long lowerBoundId, final Long upperBoundId);
}