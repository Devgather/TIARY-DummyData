package me.tiary.dummydata.service;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.domain.Verification;
import me.tiary.dummydata.repository.VerificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VerificationService {
    private final TransactionTemplate transactionTemplate;

    private final VerificationRepository verificationRepository;

    public void insertVerifications(final List<Verification> verifications) {
        transactionTemplate.executeWithoutResult(status -> {
            try {
                verificationRepository.saveAllAndFlush(verifications);
            } catch (final Exception ex) {
                status.setRollbackOnly();
                throw ex;
            }
        });
    }
}