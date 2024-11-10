package me.tiary.dummydata.generator;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.annotation.EntityGenerationLogging;
import me.tiary.dummydata.annotation.EntityInsertionLogging;
import me.tiary.dummydata.domain.Profile;
import me.tiary.dummydata.service.ProfileService;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProfileGenerator {
    private final ProfileHandler profileHandler;

    private final Faker faker;

    @Transactional
    @EntityGenerationLogging(entity = "Profile")
    public long generateProfiles(final long rows, final long batchSize) {
        final List<Profile> profiles = new ArrayList<>();
        long totalRows;

        for (totalRows = 0L; totalRows < rows; totalRows++) {
            final Profile profile = Profile.builder()
                    .nickname(generateUniqueNickname(Long.toString(totalRows)))
                    .picture(generatePicture())
                    .build();

            profiles.add(profile);

            if (profiles.size() >= batchSize) {
                profileHandler.insertProfiles(profiles);
                profiles.clear();
            }
        }

        if (!profiles.isEmpty()) {
            profileHandler.insertProfiles(profiles);
            profiles.clear();
        }

        return totalRows;
    }

    public String generateUniqueNickname(final String uniqueValue) {
        return faker.expression("#{letterify '" + "?".repeat(Profile.NICKNAME_MAX_LENGTH - uniqueValue.length()) + "'}") + uniqueValue;
    }

    public String generatePicture() {
        return faker.internet().image();
    }

    @Component
    @RequiredArgsConstructor
    public static class ProfileHandler {
        private final ProfileService profileService;

        @Transactional(propagation = Propagation.REQUIRES_NEW)
        @EntityInsertionLogging(entity = "Profile")
        public void insertProfiles(final List<Profile> profiles) {
            profileService.insertProfiles(profiles);
        }
    }
}