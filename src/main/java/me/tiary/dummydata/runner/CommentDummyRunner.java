package me.tiary.dummydata.runner;

import me.tiary.dummydata.data.Range;
import me.tiary.dummydata.generator.CommentGenerator;
import org.springframework.boot.CommandLineRunner;

public final class CommentDummyRunner implements CommandLineRunner {
    private final Range rowsRangePerTil;

    private final long batchSize;

    private final CommentGenerator commentGenerator;

    public CommentDummyRunner(final Range rowsRangePerTil, final long batchSize, final CommentGenerator commentGenerator) {
        if (batchSize <= 0) {
            throw new IllegalArgumentException("CommentDummyRunner requires at least 1 batch size");
        }

        this.rowsRangePerTil = rowsRangePerTil;
        this.batchSize = batchSize;
        this.commentGenerator = commentGenerator;
    }

    @Override
    public void run(final String... args) throws Exception {
        commentGenerator.generateComments(rowsRangePerTil, batchSize);
    }
}