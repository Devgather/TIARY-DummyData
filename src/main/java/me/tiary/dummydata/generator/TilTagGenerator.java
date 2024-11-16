package me.tiary.dummydata.generator;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.annotation.EntityGenerationLogging;
import me.tiary.dummydata.annotation.EntityInsertionLogging;
import me.tiary.dummydata.data.Range;
import me.tiary.dummydata.domain.Tag;
import me.tiary.dummydata.domain.Til;
import me.tiary.dummydata.domain.TilTag;
import me.tiary.dummydata.iterator.TilIterator;
import me.tiary.dummydata.service.TagService;
import me.tiary.dummydata.service.TilTagService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TilTagGenerator {
    private final TilTagHandler tilTagHandler;

    private final ObjectProvider<TilIterator> tilIteratorProvider;

    private final TagService tagService;

    @Transactional
    @EntityGenerationLogging(entity = "TilTag")
    public long generateTilTags(final Range rowsRangePerTil, final long batchSize) throws NoSuchAlgorithmException {
        final List<TilTag> tilTags = new ArrayList<>();
        final TilIterator tilIterator = tilIteratorProvider.getObject();
        final Range tagIdRange = tagService.findTagIdRange();
        long totalRows = 0L;

        while (tilIterator.hasNext()) {
            final Til til = tilIterator.next();
            final List<Long> tagIds = tagIdRange.generateUniqueRandomValues(rowsRangePerTil.generateRandomValue());

            for (final long tagId : tagIds) {
                final Optional<Tag> tag = tagService.findById(tagId);

                if (tag.isPresent()) {
                    final TilTag tilTag = TilTag.builder()
                            .til(til)
                            .tag(tag.get())
                            .build();

                    tilTags.add(tilTag);
                    totalRows++;
                }

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
        private final TilTagService tilTagService;

        @Transactional(propagation = Propagation.REQUIRES_NEW)
        @EntityInsertionLogging(entity = "TilTag")
        public void insertTilTags(final List<TilTag> tilTags) {
            tilTagService.insertTilTags(tilTags);
        }
    }
}