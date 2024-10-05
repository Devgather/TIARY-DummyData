package me.tiary.dummydata.repository;

import me.tiary.dummydata.domain.OAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuthRepository extends JpaRepository<OAuth, Long> {
}