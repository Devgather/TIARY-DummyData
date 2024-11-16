package me.tiary.dummydata.runner;

import me.tiary.dummydata.data.Range;
import me.tiary.dummydata.generator.TilTagGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "runner.dummy.til-tag", name = "enabled")
@Order(7)
public final class TilTagDummyRunner implements CommandLineRunner {
    private final Range rowsRangePerTil;

    private final long batchSize;

    private final TilTagGenerator tilTagGenerator;

    public TilTagDummyRunner(@Value("${runner.dummy.til-tag.rows-range-per-til}") final Range rowsRangePerTil,
                             @Value("${runner.dummy.til-tag.batch-size}") final long batchSize,
                             final TilTagGenerator tilTagGenerator) {
        if (batchSize <= 0) {
            throw new IllegalArgumentException("TilTagDummyRunner requires at least 1 batch size");
        }

        this.rowsRangePerTil = rowsRangePerTil;
        this.batchSize = batchSize;
        this.tilTagGenerator = tilTagGenerator;
    }

    @Override
    public void run(final String... args) throws Exception {
        tilTagGenerator.generateTilTags(rowsRangePerTil, batchSize);
    }
}