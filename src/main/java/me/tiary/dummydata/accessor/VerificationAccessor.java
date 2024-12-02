package me.tiary.dummydata.accessor;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.domain.Verification;
import me.tiary.dummydata.repository.VerificationRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class VerificationAccessor {
    private final VerificationRepository verificationRepository;

    public void insertVerifications(final List<Verification> verifications) {
        verificationRepository.saveBatch(verifications);
    }
}