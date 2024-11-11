package me.tiary.dummydata.generator;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.annotation.EntityGenerationLogging;
import me.tiary.dummydata.annotation.EntityInsertionLogging;
import me.tiary.dummydata.data.Range;
import me.tiary.dummydata.domain.Profile;
import me.tiary.dummydata.domain.Til;
import me.tiary.dummydata.iterator.ProfileIterator;
import me.tiary.dummydata.service.TilService;
import net.datafaker.Faker;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TilGenerator {
    private final TilHandler tilHandler;

    private final ObjectProvider<ProfileIterator> profileIteratorProvider;

    private final Faker faker;

    @Transactional
    @EntityGenerationLogging(entity = "Til")
    public long generateTils(final Range rowsRangePerProfile, final long batchSize) throws NoSuchAlgorithmException {
        final List<Til> tils = new ArrayList<>();
        final ProfileIterator profileIterator = profileIteratorProvider.getObject();
        long totalRows = 0L;

        while (profileIterator.hasNext()) {
            final Profile profile = profileIterator.next();
            final long rows = rowsRangePerProfile.generateRandomValue();

            for (long row = 0L; row < rows; row++) {
                final Til til = Til.builder()
                        .profile(profile)
                        .title(generateTitle())
                        .content(generateContent())
                        .build();

                tils.add(til);

                if (tils.size() >= batchSize) {
                    tilHandler.insertTils(tils);
                    tils.clear();
                }
            }

            totalRows += rows;
        }

        if (!tils.isEmpty()) {
            tilHandler.insertTils(tils);
            tils.clear();
        }

        return totalRows;
    }

    public String generateTitle() {
        return faker.lorem().sentence();
    }

    public String generateContent() {
        return faker.lorem().paragraph();
    }

    @Component
    @RequiredArgsConstructor
    public static class TilHandler {
        private final TilService tilService;

        @Transactional(propagation = Propagation.REQUIRES_NEW)
        @EntityInsertionLogging(entity = "Til")
        public void insertTils(final List<Til> tils) {
            tilService.insertTils(tils);
        }
    }
}