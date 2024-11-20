package me.tiary.dummydata.runner;

import me.tiary.dummydata.generator.ProfileGenerator;
import org.springframework.boot.CommandLineRunner;

public final class ProfileDummyRunner implements CommandLineRunner {
    private final long rows;

    private final long batchSize;

    private final ProfileGenerator profileGenerator;

    public ProfileDummyRunner(final long rows, final long batchSize, final ProfileGenerator profileGenerator) {
        if (rows <= 0) {
            throw new IllegalArgumentException("ProfileDummyRunner requires at least 1 row");
        }

        if (batchSize <= 0) {
            throw new IllegalArgumentException("ProfileDummyRunner requires at least 1 batch size");
        }

        this.rows = rows;
        this.batchSize = batchSize;
        this.profileGenerator = profileGenerator;
    }

    @Override
    public void run(final String... args) {
        profileGenerator.generateProfiles(rows, batchSize);
    }
}