package me.tiary.dummydata.runner;

import me.tiary.dummydata.data.Range;
import me.tiary.dummydata.generator.TilGenerator;
import org.springframework.boot.CommandLineRunner;

public final class TilDummyRunner implements CommandLineRunner {
    private final Range rowsRangePerProfile;

    private final long batchSize;

    private final TilGenerator tilGenerator;

    public TilDummyRunner(final Range rowsRangePerProfile, final long batchSize, final TilGenerator tilGenerator) {
        if (batchSize <= 0) {
            throw new IllegalArgumentException("TilDummyRunner requires at least 1 batch size");
        }

        this.rowsRangePerProfile = rowsRangePerProfile;
        this.batchSize = batchSize;
        this.tilGenerator = tilGenerator;
    }

    @Override
    public void run(final String... args) {
        tilGenerator.generateTils(rowsRangePerProfile, batchSize);
    }
}