package me.tiary.dummydata.generator;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.accessor.CommentAccessor;
import me.tiary.dummydata.annotation.EntityGenerationLogging;
import me.tiary.dummydata.annotation.EntityInsertionLogging;
import me.tiary.dummydata.data.Range;
import me.tiary.dummydata.domain.Comment;
import me.tiary.dummydata.domain.Profile;
import me.tiary.dummydata.domain.Til;
import me.tiary.dummydata.iterator.ProfileRandomIterator;
import me.tiary.dummydata.iterator.TilIterator;
import me.tiary.dummydata.iterator.factory.ProfileRandomIteratorFactory;
import me.tiary.dummydata.iterator.factory.TilIteratorFactory;
import net.datafaker.Faker;
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
public class CommentGenerator {
    private final CommentHandler commentHandler;

    private final TilIteratorFactory tilIteratorFactory;

    private final ProfileRandomIteratorFactory profileRandomIteratorFactory;

    private final Faker faker;

    @Transactional
    @EntityGenerationLogging(entity = "Comment")
    public long generateComments(final Range rowsRangePerTil, final long batchSize) {
        final List<Comment> comments = new ArrayList<>();
        final TilIterator tilIterator = tilIteratorFactory.create(batchSize);
        final ProfileRandomIterator profileRandomIterator = profileRandomIteratorFactory.create(batchSize, ProfileRandomIterator.DEFAULT_MAX_FETCH_ATTEMPTS, 10.0 / batchSize);
        long totalRows = 0L;

        while (tilIterator.hasNext()) {
            final Til til = tilIterator.next();
            final List<Profile> profiles = StreamSupport.stream(Spliterators.spliteratorUnknownSize(profileRandomIterator, Spliterator.ORDERED | Spliterator.NONNULL), false)
                    .limit(rowsRangePerTil.generateRandomValue())
                    .toList();

            for (final Profile profile : profiles) {
                final Comment comment = Comment.builder()
                        .profile(profile)
                        .til(til)
                        .content(generateContent())
                        .build();

                comments.add(comment);
                totalRows++;

                if (comments.size() >= batchSize) {
                    commentHandler.insertComments(comments);
                    comments.clear();
                }
            }
        }

        if (!comments.isEmpty()) {
            commentHandler.insertComments(comments);
            comments.clear();
        }

        return totalRows;
    }

    public String generateContent() {
        return faker.lorem().sentence();
    }

    @Component
    @RequiredArgsConstructor
    public static class CommentHandler {
        private final CommentAccessor commentAccessor;

        @Transactional(propagation = Propagation.REQUIRES_NEW)
        @EntityInsertionLogging(entity = "Comment")
        public void insertComments(final List<Comment> comments) {
            commentAccessor.insertComments(comments);
        }
    }
}