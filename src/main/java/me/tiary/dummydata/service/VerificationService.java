package me.tiary.dummydata.service;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.domain.Verification;
import me.tiary.dummydata.repository.VerificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VerificationService {
    private final VerificationRepository verificationRepository;

    public void insertVerifications(final List<Verification> verifications) {
        verificationRepository.saveAll(verifications);
    }
}