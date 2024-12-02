package me.tiary.dummydata.repository;

import me.tiary.dummydata.domain.Verification;
import me.tiary.dummydata.repository.custom.VerificationCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationRepository extends JpaRepository<Verification, Long>, VerificationCustomRepository {
}