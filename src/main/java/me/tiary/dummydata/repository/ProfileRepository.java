package me.tiary.dummydata.repository;

import me.tiary.dummydata.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    @Query("select p from Profile p left join fetch p.oAuths where p.id = :id")
    Optional<Profile> findLeftJoinFetchOAuthById(@Param("id") final Long id);

    @Query("select p from Profile p left join fetch p.tils where p.id = :id")
    Optional<Profile> findLeftJoinFetchTilById(@Param("id") final Long id);

    Optional<Profile> findFirstByOrderByIdAsc();

    @Query("select p from Profile p where p.id > :previousId order by p.id asc limit 1")
    Optional<Profile> findNextByPreviousId(@Param("previousId") final Long previousId);
}