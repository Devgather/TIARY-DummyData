package me.tiary.dummydata.repository.custom;

import me.tiary.dummydata.domain.Verification;

import java.util.List;

public interface VerificationCustomRepository {
    void saveBatch(final List<Verification> verifications);
}