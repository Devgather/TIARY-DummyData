package me.tiary.dummydata.generator;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.annotation.EntityGenerationLogging;
import me.tiary.dummydata.annotation.EntityInsertionLogging;
import me.tiary.dummydata.data.Range;
import me.tiary.dummydata.domain.Comment;
import me.tiary.dummydata.domain.Profile;
import me.tiary.dummydata.domain.Til;
import me.tiary.dummydata.iterator.TilIterator;
import me.tiary.dummydata.service.CommentService;
import me.tiary.dummydata.service.ProfileService;
import net.datafaker.Faker;
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
public class CommentGenerator {
    private final CommentHandler commentHandler;

    private final ObjectProvider<TilIterator> tilIteratorProvider;

    private final ProfileService profileService;

    private final Faker faker;

    @Transactional
    @EntityGenerationLogging(entity = "Comment")
    public long generateComments(final Range rowsRangePerTil, final long batchSize) throws NoSuchAlgorithmException {
        final List<Comment> comments = new ArrayList<>();
        final TilIterator tilIterator = tilIteratorProvider.getObject();
        final Range profileIdRange = profileService.findProfileIdRange();
        long totalRows = 0L;

        while (tilIterator.hasNext()) {
            final Til til = tilIterator.next();
            final List<Long> profileIds = profileIdRange.generateRandomValues(rowsRangePerTil.generateRandomValue());

            for (final long profileId : profileIds) {
                final Optional<Profile> profile = profileService.findById(profileId);

                if (profile.isPresent()) {
                    final Comment comment = Comment.builder()
                            .profile(profile.get())
                            .til(til)
                            .content(generateContent())
                            .build();

                    comments.add(comment);
                    totalRows++;
                }

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
        private final CommentService commentService;

        @Transactional(propagation = Propagation.REQUIRES_NEW)
        @EntityInsertionLogging(entity = "Comment")
        public void insertComments(final List<Comment> comments) {
            commentService.insertComments(comments);
        }
    }
}