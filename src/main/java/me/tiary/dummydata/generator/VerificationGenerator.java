package me.tiary.dummydata.generator;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.accessor.VerificationAccessor;
import me.tiary.dummydata.annotation.EntityGenerationLogging;
import me.tiary.dummydata.annotation.EntityInsertionLogging;
import me.tiary.dummydata.domain.Verification;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class VerificationGenerator {
    private static final Random random;

    private final VerificationHandler verificationHandler;

    private final Faker faker;

    static {
        try {
            random = SecureRandom.getInstanceStrong();
        } catch (final NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Failed to initialize VerificationGenerator", ex);
        }
    }

    @Transactional
    @EntityGenerationLogging(entity = "Verification")
    public long generateVerifications(final long rows, final long batchSize) {
        final List<Verification> verifications = new ArrayList<>();
        long totalRows;

        for (totalRows = 0L; totalRows < rows; totalRows++) {
            final Verification verification = Verification.builder()
                    .email(generateUniqueEmail(Long.toString(totalRows)))
                    .code(generateCode())
                    .state(generateState())
                    .build();

            verifications.add(verification);

            if (verifications.size() >= batchSize) {
                verificationHandler.insertVerifications(verifications);
                verifications.clear();
            }
        }

        if (!verifications.isEmpty()) {
            verificationHandler.insertVerifications(verifications);
            verifications.clear();
        }

        return totalRows;
    }

    public String generateUniqueEmail(final String uniqueValue) {
        return faker.internet().emailAddress(faker.internet().username() + uniqueValue);
    }

    public String generateCode() {
        return faker.expression("#{letterify '" + "?".repeat(Verification.CODE_MAX_LENGTH) + "'}");
    }

    public boolean generateState() {
        return random.nextBoolean();
    }

    @Component
    @RequiredArgsConstructor
    public static class VerificationHandler {
        private final VerificationAccessor verificationAccessor;

        @Transactional(propagation = Propagation.REQUIRES_NEW)
        @EntityInsertionLogging(entity = "Verification")
        public void insertVerifications(final List<Verification> verifications) {
            verificationAccessor.insertVerifications(verifications);
        }
    }
}