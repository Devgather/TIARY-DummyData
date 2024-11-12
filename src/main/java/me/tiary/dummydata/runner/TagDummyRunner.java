package me.tiary.dummydata.runner;

import me.tiary.dummydata.generator.TagGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "runner.dummy.tag", name = "enabled")
@Order(6)
public final class TagDummyRunner implements CommandLineRunner {
    private final long rows;

    private final long batchSize;

    private final TagGenerator tagGenerator;

    public TagDummyRunner(@Value("${runner.dummy.tag.rows}") final long rows,
                          @Value("${runner.dummy.tag.batch-size}") final long batchSize,
                          final TagGenerator tagGenerator) {
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