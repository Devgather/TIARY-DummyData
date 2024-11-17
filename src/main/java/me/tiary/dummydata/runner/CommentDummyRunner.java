package me.tiary.dummydata.runner;

import me.tiary.dummydata.data.Range;
import me.tiary.dummydata.generator.CommentGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "runner.dummy.comment", name = "enabled")
@Order(8)
public final class CommentDummyRunner implements CommandLineRunner {
    private final Range rowsRangePerTil;

    private final long batchSize;

    private final CommentGenerator commentGenerator;

    public CommentDummyRunner(@Value("${runner.dummy.comment.rows-range-per-til}") final Range rowsRangePerTil,
                              @Value("${runner.dummy.comment.batch-size}") final long batchSize,
                              final CommentGenerator commentGenerator) {
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