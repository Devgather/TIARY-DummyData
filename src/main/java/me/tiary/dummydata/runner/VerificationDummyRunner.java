package me.tiary.dummydata.runner;

import me.tiary.dummydata.generator.VerificationGenerator;
import org.springframework.boot.CommandLineRunner;

public final class VerificationDummyRunner implements CommandLineRunner {
    private final long rows;

    private final long batchSize;

    private final VerificationGenerator verificationGenerator;

    public VerificationDummyRunner(final long rows, final long batchSize, final VerificationGenerator verificationGenerator) {
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