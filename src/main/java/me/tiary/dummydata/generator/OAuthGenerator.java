package me.tiary.dummydata.generator;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.accessor.OAuthAccessor;
import me.tiary.dummydata.annotation.EntityGenerationLogging;
import me.tiary.dummydata.annotation.EntityInsertionLogging;
import me.tiary.dummydata.data.Range;
import me.tiary.dummydata.domain.OAuth;
import me.tiary.dummydata.domain.Profile;
import me.tiary.dummydata.iterator.ProfileIterator;
import me.tiary.dummydata.iterator.factory.ProfileIteratorFactory;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OAuthGenerator {
    private final OAuthHandler oAuthHandler;

    private final ProfileIteratorFactory profileIteratorFactory;

    private final Faker faker;

    @Transactional
    @EntityGenerationLogging(entity = "OAuth")
    public long generateOAuths(final Range rowsRangePerProfile, final long batchSize) throws NoSuchAlgorithmException {
        final List<OAuth> oAuths = new ArrayList<>();
        final ProfileIterator profileIterator = profileIteratorFactory.create(batchSize);
        long totalRows = 0L;

        while (profileIterator.hasNext()) {
            final Profile profile = profileIterator.next();
            final long rows = rowsRangePerProfile.generateRandomValue();

            for (long row = 0L; row < rows; row++) {
                final OAuth oAuth = OAuth.builder()
                        .profile(profile)
                        .identifier(generateUniqueIdentifier(Long.toString(totalRows + row)))
                        .provider(generateProvider())
                        .build();

                oAuths.add(oAuth);

                if (oAuths.size() >= batchSize) {
                    oAuthHandler.insertOAuths(oAuths);
                    oAuths.clear();
                }
            }

            totalRows += rows;
        }

        if (!oAuths.isEmpty()) {
            oAuthHandler.insertOAuths(oAuths);
            oAuths.clear();
        }

        return totalRows;
    }

    public String generateUniqueIdentifier(final String uniqueValue) {
        return faker.expression("#{numerify '####################'}") + uniqueValue;
    }

    public String generateProvider() {
        return faker.expression("#{options.option 'apple','github','google','kakao','naver'}");
    }

    @Component
    @RequiredArgsConstructor
    public static class OAuthHandler {
        private final OAuthAccessor oAuthAccessor;

        @Transactional(propagation = Propagation.REQUIRES_NEW)
        @EntityInsertionLogging(entity = "OAuth")
        public void insertOAuths(final List<OAuth> oAuths) {
            oAuthAccessor.insertOAuths(oAuths);
        }
    }
}