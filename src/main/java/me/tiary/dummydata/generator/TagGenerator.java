package me.tiary.dummydata.generator;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.accessor.TagAccessor;
import me.tiary.dummydata.annotation.EntityGenerationLogging;
import me.tiary.dummydata.annotation.EntityInsertionLogging;
import me.tiary.dummydata.domain.Tag;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TagGenerator {
    private final TagHandler tagHandler;

    private final Faker faker;

    @Transactional
    @EntityGenerationLogging(entity = "Tag")
    public long generateTags(final long rows, final long batchSize) {
        final List<Tag> tags = new ArrayList<>();
        long totalRows;

        for (totalRows = 0L; totalRows < rows; totalRows++) {
            final Tag tag = Tag.builder()
                    .name(generateUniqueName(Long.toString(totalRows)))
                    .build();

            tags.add(tag);

            if (tags.size() >= batchSize) {
                tagHandler.insertTags(tags);
                tags.clear();
            }
        }

        if (!tags.isEmpty()) {
            tagHandler.insertTags(tags);
            tags.clear();
        }

        return totalRows;
    }

    public String generateUniqueName(final String uniqueValue) {
        return faker.lorem().word() + uniqueValue;
    }

    @Component
    @RequiredArgsConstructor
    public static class TagHandler {
        private final TagAccessor tagAccessor;

        @Transactional(propagation = Propagation.REQUIRES_NEW)
        @EntityInsertionLogging(entity = "Tag")
        public void insertTags(final List<Tag> tags) {
            tagAccessor.insertTags(tags);
        }
    }
}