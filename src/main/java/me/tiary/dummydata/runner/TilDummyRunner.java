package me.tiary.dummydata.runner;

import me.tiary.dummydata.data.Range;
import me.tiary.dummydata.generator.TilGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "runner.dummy.til", name = "enabled")
@Order(5)
public final class TilDummyRunner implements CommandLineRunner {
    private final Range rowsRangePerProfile;

    private final long batchSize;

    private final TilGenerator tilGenerator;

    public TilDummyRunner(@Value("${runner.dummy.til.rows-range-per-profile}") final Range rowsRangePerProfile,
                          @Value("${runner.dummy.til.batch-size}") final long batchSize,
                          final TilGenerator tilGenerator) {
        if (batchSize <= 0) {
            throw new IllegalArgumentException("TilDummyRunner requires at least 1 batch size");
        }

        this.rowsRangePerProfile = rowsRangePerProfile;
        this.batchSize = batchSize;
        this.tilGenerator = tilGenerator;
    }

    @Override
    public void run(final String... args) throws Exception {
        tilGenerator.generateTils(rowsRangePerProfile, batchSize);
    }
}