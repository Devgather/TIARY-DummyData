package me.tiary.dummydata.runner;

import me.tiary.dummydata.generator.ProfileGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "runner.dummy.profile", name = "enabled")
@Order(1)
public final class ProfileDummyRunner implements CommandLineRunner {
    private final long rows;

    private final long batchSize;

    private final ProfileGenerator profileGenerator;

    public ProfileDummyRunner(@Value("${runner.dummy.profile.rows}") final long rows,
                              @Value("${runner.dummy.profile.batch-size}") final long batchSize,
                              final ProfileGenerator profileGenerator) {
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