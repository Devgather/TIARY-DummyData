package me.tiary.dummydata.runner;

import lombok.extern.slf4j.Slf4j;
import me.tiary.dummydata.data.Range;
import me.tiary.dummydata.domain.Tag;
import me.tiary.dummydata.domain.Til;
import me.tiary.dummydata.domain.TilTag;
import me.tiary.dummydata.service.TagService;
import me.tiary.dummydata.service.TilService;
import me.tiary.dummydata.service.TilTagService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@ConditionalOnProperty(prefix = "runner.dummy.til-tag", name = "enabled")
@Order(7)
@Slf4j
public final class TilTagDummyRunner implements CommandLineRunner {
    private final Range rowsRangePerTil;

    private final long batchSize;

    private final TilTagService tilTagService;

    private final TilService tilService;

    private final TagService tagService;

    public TilTagDummyRunner(@Value("${runner.dummy.til-tag.rows-range-per-til}") final Range rowsRangePerTil,
                             @Value("${runner.dummy.til-tag.batch-size}") final long batchSize,
                             final TilTagService tilTagService,
                             final TilService tilService,
                             final TagService tagService) {
        if (batchSize <= 0) {
            throw new IllegalArgumentException("TilTagDummyRunner requires at least 1 batch size");
        }

        this.rowsRangePerTil = rowsRangePerTil;
        this.batchSize = batchSize;
        this.tilTagService = tilTagService;
        this.tilService = tilService;
        this.tagService = tagService;
    }

    @Override
    public void run(final String... args) throws Exception {
        final long tagMinimumId = tagService.findTagMinimumId();
        final long tagMaximumId = tagService.findTagMaximumId();
        final long tagsNumber = tagMaximumId - tagMinimumId + 1;

        if (rowsRangePerTil.upperBound() > tagsNumber) {
            throw new IllegalArgumentException("TilTagDummyRunner requires upper bound of rows range per TIL to be less than or equal to number of Tags");
        }

        final long tilMinimumId = tilService.findTilMinimumId();
        final List<TilTag> tilTags = new ArrayList<>();
        long tilRow = 0L;
        long tilTagRow = 0L;

        while (true) {
            final Optional<Til> til = tilService.findById(tilMinimumId + tilRow++);

            if (til.isPresent()) {
                final long tagRows = rowsRangePerTil.generateRandomValue();

                for (long row = 0L; row < tagRows; row++) {
                    final long tagIdLowerBound = row * tagsNumber / tagRows + tagMinimumId;
                    final long tagIdUpperBound = tagIdLowerBound + tagsNumber / tagRows - 1;
                    final Range tagIdRange = new Range(tagIdLowerBound, tagIdUpperBound);
                    final Optional<Tag> tag = tagService.findById(tagIdRange.generateRandomValue());

                    if (tag.isPresent()) {
                        final TilTag tilTag = TilTag.builder()
                                .til(til.get())
                                .tag(tag.get())
                                .build();

                        tilTags.add(tilTag);
                        tilTagRow++;

                        if (tilTags.size() >= batchSize) {
                            try {
                                tilTagService.insertTilTags(tilTags);
                                log.info("Inserted dummy TilTags: {} rows", NumberFormat.getInstance().format(tilTags.size()));
                                tilTags.clear();
                            } catch (final Exception ex) {
                                log.error("Failed to insert dummy TilTags: {}", ex.getMessage());
                            }
                        }
                    }
                }
            }

            if (til.isEmpty() && !tilTags.isEmpty()) {
                try {
                    tilTagService.insertTilTags(tilTags);
                    log.info("Inserted dummy TilTags: {} rows", NumberFormat.getInstance().format(tilTags.size()));
                    tilTags.clear();
                } catch (final Exception ex) {
                    log.error("Failed to insert dummy TilTags: {}", ex.getMessage());
                }
            }

            if (til.isEmpty()) {
                log.info("Finished dummy TilTags insertion: {} rows", NumberFormat.getInstance().format(tilTagRow));
                break;
            }
        }
    }
}