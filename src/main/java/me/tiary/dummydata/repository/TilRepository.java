package me.tiary.dummydata.repository;

import me.tiary.dummydata.domain.Til;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TilRepository extends JpaRepository<Til, Long> {
    Optional<Til> findFirstByOrderByIdAsc();

    Optional<Til> findFirstByOrderByIdDesc();
}