package me.tiary.dummydata.runner;

import lombok.extern.slf4j.Slf4j;
import me.tiary.dummydata.domain.Verification;
import me.tiary.dummydata.service.VerificationService;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

@Component
@ConditionalOnProperty(prefix = "runner.dummy.verification", name = "enabled")
@Order(3)
@Slf4j
public final class VerificationDummyRunner implements CommandLineRunner {
    private final long rows;

    private final long batchSize;

    private final VerificationService verificationService;

    private final Faker faker;

    public VerificationDummyRunner(@Value("${runner.dummy.verification.rows}") final long rows,
                                   @Value("${runner.dummy.verification.batch-size}") final long batchSize,
                                   final VerificationService verificationService,
                                   final Faker faker) {
        this.rows = rows;
        this.batchSize = batchSize;
        this.verificationService = verificationService;
        this.faker = faker;
    }

    @Override
    public void run(final String... args) throws Exception {
        if (rows <= 0) {
            throw new IllegalStateException("VerificationDummyRunner requires at least 1 row");
        }

        if (batchSize <= 0) {
            throw new IllegalStateException("VerificationDummyRunner requires at least 1 batch size");
        }

        final List<Verification> verifications = new ArrayList<>();
        long row = 0L;

        while (true) {
            final Verification verification = Verification.builder()
                    .email(generateUniqueEmail(row))
                    .code(generateCode())
                    .state(SecureRandom.getInstanceStrong().nextBoolean())
                    .build();

            verifications.add(verification);
            row++;

            if (verifications.size() >= batchSize || row >= rows) {
                try {
                    verificationService.insertVerifications(verifications);
                    log.info("Inserted dummy Verifications: {} rows", NumberFormat.getInstance().format(verifications.size()));
                    verifications.clear();
                } catch (final Exception ex) {
                    log.error("Failed to insert dummy Verifications: {}", ex.getMessage());
                }
            }

            if (row >= rows) {
                log.info("Finished dummy Verifications insertion: {} rows", NumberFormat.getInstance().format(row));
                break;
            }
        }
    }

    private String generateUniqueEmail(final long row) {
        return faker.internet().emailAddress(faker.internet().username() + row);
    }

    private String generateCode() {
        return faker.expression("#{letterify '" + "?".repeat(Verification.CODE_MAX_LENGTH) + "'}");
    }
}