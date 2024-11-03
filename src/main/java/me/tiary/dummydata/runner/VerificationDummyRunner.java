package me.tiary.dummydata.runner;

import me.tiary.dummydata.generator.VerificationGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "runner.dummy.verification", name = "enabled")
@Order(3)
public final class VerificationDummyRunner implements CommandLineRunner {
    private final long rows;

    private final long batchSize;

    private final VerificationGenerator verificationGenerator;

    public VerificationDummyRunner(@Value("${runner.dummy.verification.rows}") final long rows,
                                   @Value("${runner.dummy.verification.batch-size}") final long batchSize,
                                   final VerificationGenerator verificationGenerator) {
        if (rows <= 0) {
            throw new IllegalArgumentException("VerificationDummyRunner requires at least 1 row");
        }

        if (batchSize <= 0) {
            throw new IllegalArgumentException("VerificationDummyRunner requires at least 1 batch size");
        }

        this.rows = rows;
        this.batchSize = batchSize;
        this.verificationGenerator = verificationGenerator;
    }

    @Override
    public void run(final String... args) throws Exception {
        verificationGenerator.generateVerifications(rows, batchSize);
    }
}