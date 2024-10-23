package me.tiary.dummydata.runner;

import lombok.extern.slf4j.Slf4j;
import me.tiary.dummydata.domain.Tag;
import me.tiary.dummydata.service.TagService;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

@Component
@ConditionalOnProperty(prefix = "runner.dummy.tag", name = "enabled")
@Order(6)
@Slf4j
public final class TagDummyRunner implements CommandLineRunner {
    private final long rows;

    private final long batchSize;

    private final TagService tagService;

    private final Faker faker;

    public TagDummyRunner(@Value("${runner.dummy.tag.rows}") final long rows,
                          @Value("${runner.dummy.tag.batch-size}") final long batchSize,
                          final TagService tagService,
                          final Faker faker) {
        if (rows <= 0) {
            throw new IllegalArgumentException("TagDummyRunner requires at least 1 row");
        }

        if (batchSize <= 0) {
            throw new IllegalArgumentException("TagDummyRunner requires at least 1 batch size");
        }

        this.rows = rows;
        this.batchSize = batchSize;
        this.tagService = tagService;
        this.faker = faker;
    }

    @Override
    public void run(final String... args) {
        final List<Tag> tags = new ArrayList<>();
        long row = 0L;

        while (true) {
            final Tag tag = Tag.builder()
                    .name(generateUniqueName(row))
                    .build();

            tags.add(tag);
            row++;

            if (tags.size() >= batchSize || row >= rows) {
                try {
                    tagService.insertTags(tags);
                    log.info("Inserted dummy Tags: {} rows", NumberFormat.getInstance().format(tags.size()));
                    tags.clear();
                } catch (final Exception ex) {
                    log.error("Failed to insert dummy Tags: {}", ex.getMessage());
                }
            }

            if (row >= rows) {
                log.info("Finished dummy Tags insertion: {} rows", NumberFormat.getInstance().format(row));
                break;
            }
        }
    }

    private String generateUniqueName(final long row) {
        return faker.lorem().word() + row;
    }
}