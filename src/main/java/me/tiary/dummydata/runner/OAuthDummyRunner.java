package me.tiary.dummydata.runner;

import me.tiary.dummydata.data.Range;
import me.tiary.dummydata.generator.OAuthGenerator;
import org.springframework.boot.CommandLineRunner;

public final class OAuthDummyRunner implements CommandLineRunner {
    private final Range rowsRangePerProfile;

    private final long batchSize;

    private final OAuthGenerator oAuthGenerator;

    public OAuthDummyRunner(final Range rowsRangePerProfile, final long batchSize, final OAuthGenerator oAuthGenerator) {
        if (batchSize <= 0) {
            throw new IllegalArgumentException("OAuthDummyRunner requires at least 1 batch size");
        }

        this.rowsRangePerProfile = rowsRangePerProfile;
        this.batchSize = batchSize;
        this.oAuthGenerator = oAuthGenerator;
    }

    @Override
    public void run(final String... args) {
        oAuthGenerator.generateOAuths(rowsRangePerProfile, batchSize);
    }
}