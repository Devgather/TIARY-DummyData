package me.tiary.dummydata.repository;

import me.tiary.dummydata.domain.Profile;
import me.tiary.dummydata.repository.custom.ProfileCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long>, ProfileCustomRepository {
    Optional<Profile> findFirstByOrderByIdAsc();

    Optional<Profile> findFirstByOrderByIdDesc();

    List<Profile> findAllByIdBetween(final Long lowerBoundId, final Long upperBoundId);
}