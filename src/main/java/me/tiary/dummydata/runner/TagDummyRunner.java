package me.tiary.dummydata.runner;

import me.tiary.dummydata.generator.TagGenerator;
import org.springframework.boot.CommandLineRunner;

public final class TagDummyRunner implements CommandLineRunner {
    private final long rows;

    private final long batchSize;

    private final TagGenerator tagGenerator;

    public TagDummyRunner(final long rows, final long batchSize, final TagGenerator tagGenerator) {
        if (rows <= 0) {
            throw new IllegalArgumentException("TagDummyRunner requires at least 1 row");
        }

        if (batchSize <= 0) {
            throw new IllegalArgumentException("TagDummyRunner requires at least 1 batch size");
        }

        this.rows = rows;
        this.batchSize = batchSize;
        this.tagGenerator = tagGenerator;
    }

    @Override
    public void run(final String... args) {
        tagGenerator.generateTags(rows, batchSize);
    }
}