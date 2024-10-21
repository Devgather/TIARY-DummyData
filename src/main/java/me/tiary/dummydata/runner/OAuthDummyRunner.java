package me.tiary.dummydata.runner;

import lombok.extern.slf4j.Slf4j;
import me.tiary.dummydata.data.Range;
import me.tiary.dummydata.domain.OAuth;
import me.tiary.dummydata.domain.Profile;
import me.tiary.dummydata.service.OAuthService;
import me.tiary.dummydata.service.ProfileService;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Component
@ConditionalOnProperty(prefix = "runner.dummy.oauth", name = "enabled")
@Order(4)
@Slf4j
public final class OAuthDummyRunner implements CommandLineRunner {
    private final Range rowsRangePerProfile;

    private final long batchSize;

    private final OAuthService oAuthService;

    private final ProfileService profileService;

    private final Faker faker;

    public OAuthDummyRunner(@Value("${runner.dummy.oauth.rows-range-per-profile}") final Range rowsRangePerProfile,
                            @Value("${runner.dummy.oauth.batch-size}") final long batchSize,
                            final OAuthService oAuthService,
                            final ProfileService profileService,
                            final Faker faker) {
        this.rowsRangePerProfile = rowsRangePerProfile;
        this.batchSize = batchSize;
        this.oAuthService = oAuthService;
        this.profileService = profileService;
        this.faker = faker;
    }

    @Override
    public void run(final String... args) throws Exception {
        if (batchSize <= 0) {
            throw new IllegalStateException("OAuthDummyRunner requires at least 1 batch size");
        }

        final long profileMinimumId = profileService.findProfileMinimumId();
        final List<OAuth> oAuths = new ArrayList<>();
        long profileRow = 0L;
        long oAuthRow = 0L;

        while (true) {
            final Optional<Profile> profile = profileService.findWithOAuthById(profileMinimumId + profileRow++);

            if (profile.isPresent()) {
                final Random random = SecureRandom.getInstanceStrong();
                final long oAuthRows = random.nextLong(rowsRangePerProfile.upperBound() - rowsRangePerProfile.lowerBound() + 1) + rowsRangePerProfile.lowerBound();

                for (long row = 0L; row < oAuthRows; row++) {
                    final OAuth oAuth = OAuth.builder()
                            .profile(profile.get())
                            .identifier(generateUniqueIdentifier(oAuthRow))
                            .provider(generateProvider())
                            .build();

                    oAuths.add(oAuth);
                    oAuthRow++;

                    if (oAuths.size() >= batchSize) {
                        try {
                            oAuthService.insertOAuths(oAuths);
                            log.info("Inserted dummy OAuths: {} rows", NumberFormat.getInstance().format(oAuths.size()));
                            oAuths.clear();
                        } catch (final Exception ex) {
                            log.error("Failed to insert dummy OAuths: {}", ex.getMessage());
                        }
                    }
                }
            }

            if (profile.isEmpty() && !oAuths.isEmpty()) {
                try {
                    oAuthService.insertOAuths(oAuths);
                    log.info("Inserted dummy OAuths: {} rows", NumberFormat.getInstance().format(oAuths.size()));
                    oAuths.clear();
                } catch (final Exception ex) {
                    log.error("Failed to insert dummy OAuths: {}", ex.getMessage());
                }
            }

            if (profile.isEmpty()) {
                log.info("Finished dummy OAuths insertion: {} rows", NumberFormat.getInstance().format(oAuthRow));
                break;
            }
        }
    }

    private String generateUniqueIdentifier(final long row) {
        return faker.expression("#{numerify '####################'}" + row);
    }

    private String generateProvider() {
        return faker.expression("#{options.option 'apple','github','google','kakao'}");
    }
}