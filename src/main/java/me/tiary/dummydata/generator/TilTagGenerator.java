package me.tiary.dummydata.generator;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.accessor.TilTagAccessor;
import me.tiary.dummydata.annotation.EntityGenerationLogging;
import me.tiary.dummydata.annotation.EntityInsertionLogging;
import me.tiary.dummydata.data.Range;
import me.tiary.dummydata.domain.Tag;
import me.tiary.dummydata.domain.Til;
import me.tiary.dummydata.domain.TilTag;
import me.tiary.dummydata.iterator.TagRandomIterator;
import me.tiary.dummydata.iterator.TilIterator;
import me.tiary.dummydata.iterator.factory.TagRandomIteratorFactory;
import me.tiary.dummydata.iterator.factory.TilIteratorFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

@Component
@RequiredArgsConstructor
public class TilTagGenerator {
    private final TilTagHandler tilTagHandler;

    private final TilIteratorFactory tilIteratorFactory;

    private final TagRandomIteratorFactory tagRandomIteratorFactory;

    @Transactional
    @EntityGenerationLogging(entity = "TilTag")
    public long generateTilTags(final Range rowsRangePerTil, final long batchSize) {
        final List<TilTag> tilTags = new ArrayList<>();
        final TilIterator tilIterator = tilIteratorFactory.create(batchSize);
        final TagRandomIterator tagRandomIterator = tagRandomIteratorFactory.create(batchSize, TagRandomIterator.DEFAULT_MAX_FETCH_ATTEMPTS);
        long totalRows = 0L;

        while (tilIterator.hasNext()) {
            final Til til = tilIterator.next();
            final List<Tag> tags = StreamSupport.stream(Spliterators.spliteratorUnknownSize(tagRandomIterator, Spliterator.ORDERED | Spliterator.NONNULL), false)
                    .distinct()
                    .limit(rowsRangePerTil.generateRandomValue())
                    .toList();

            for (final Tag tag : tags) {
                final TilTag tilTag = TilTag.builder()
                        .til(til)
                        .tag(tag)
                        .build();

                tilTags.add(tilTag);
                totalRows++;

                if (tilTags.size() >= batchSize) {
                    tilTagHandler.insertTilTags(tilTags);
                    tilTags.clear();
                }
            }
        }

        if (!tilTags.isEmpty()) {
            tilTagHandler.insertTilTags(tilTags);
            tilTags.clear();
        }

        return totalRows;
    }

    @Component
    @RequiredArgsConstructor
    public static class TilTagHandler {
        private final TilTagAccessor tilTagAccessor;

        @Transactional(propagation = Propagation.REQUIRES_NEW)
        @EntityInsertionLogging(entity = "TilTag")
        public void insertTilTags(final List<TilTag> tilTags) {
            tilTagAccessor.insertTilTags(tilTags);
        }
    }
}