package me.tiary.dummydata.repository;

import me.tiary.dummydata.domain.Tag;
import me.tiary.dummydata.repository.custom.TagCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long>, TagCustomRepository {
    Optional<Tag> findFirstByOrderByIdAsc();

    Optional<Tag> findFirstByOrderByIdDesc();

    List<Tag> findAllByIdBetween(final Long lowerBoundId, final Long upperBoundId);
}