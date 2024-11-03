package me.tiary.dummydata.generator;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.annotation.EntityGenerationLogging;
import me.tiary.dummydata.annotation.EntityInsertionLogging;
import me.tiary.dummydata.domain.Verification;
import me.tiary.dummydata.service.VerificationService;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class VerificationGenerator {
    private final VerificationHandler verificationHandler;

    private final Faker faker;

    @EntityGenerationLogging(entity = "Verification")
    public long generateVerifications(final long rows, final long batchSize) throws NoSuchAlgorithmException {
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

    public boolean generateState() throws NoSuchAlgorithmException {
        return SecureRandom.getInstanceStrong().nextBoolean();
    }

    @Component
    @RequiredArgsConstructor
    public static class VerificationHandler {
        private final VerificationService verificationService;

        @EntityInsertionLogging(entity = "Verification")
        public void insertVerifications(final List<Verification> verifications) {
            verificationService.insertVerifications(verifications);
        }
    }
}