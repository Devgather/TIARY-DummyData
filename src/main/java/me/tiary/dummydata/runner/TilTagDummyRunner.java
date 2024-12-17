package me.tiary.dummydata.runner;

import me.tiary.dummydata.data.Range;
import me.tiary.dummydata.generator.TilTagGenerator;
import org.springframework.boot.CommandLineRunner;

public final class TilTagDummyRunner implements CommandLineRunner {
    private final Range rowsRangePerTil;

    private final long batchSize;

    private final TilTagGenerator tilTagGenerator;

    public TilTagDummyRunner(final Range rowsRangePerTil, final long batchSize, final TilTagGenerator tilTagGenerator) {
        if (batchSize <= 0) {
            throw new IllegalArgumentException("TilTagDummyRunner requires at least 1 batch size");
        }

        this.rowsRangePerTil = rowsRangePerTil;
        this.batchSize = batchSize;
        this.tilTagGenerator = tilTagGenerator;
    }

    @Override
    public void run(final String... args) {
        tilTagGenerator.generateTilTags(rowsRangePerTil, batchSize);
    }
}