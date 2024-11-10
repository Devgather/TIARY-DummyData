package me.tiary.dummydata.runner;

import me.tiary.dummydata.data.Range;
import me.tiary.dummydata.generator.OAuthGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "runner.dummy.oauth", name = "enabled")
@Order(4)
public final class OAuthDummyRunner implements CommandLineRunner {
    private final Range rowsRangePerProfile;

    private final long batchSize;

    private final OAuthGenerator oAuthGenerator;

    public OAuthDummyRunner(@Value("${runner.dummy.oauth.rows-range-per-profile}") final Range rowsRangePerProfile,
                            @Value("${runner.dummy.oauth.batch-size}") final long batchSize,
                            final OAuthGenerator oAuthGenerator) {
        if (batchSize <= 0) {
            throw new IllegalArgumentException("OAuthDummyRunner requires at least 1 batch size");
        }

        this.rowsRangePerProfile = rowsRangePerProfile;
        this.batchSize = batchSize;
        this.oAuthGenerator = oAuthGenerator;
    }

    @Override
    public void run(final String... args) throws Exception {
        oAuthGenerator.generateOAuths(rowsRangePerProfile, batchSize);
    }
}