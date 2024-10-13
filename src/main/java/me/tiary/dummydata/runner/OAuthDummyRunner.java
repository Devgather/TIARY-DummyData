package me.tiary.dummydata.runner;

import lombok.extern.slf4j.Slf4j;
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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@ConditionalOnProperty(prefix = "runner.dummy.oauth", name = "enabled")
@Order(4)
@Slf4j
public final class OAuthDummyRunner implements CommandLineRunner {
    private final long rows;

    private final long batchSize;

    private final OAuthService oAuthService;

    private final ProfileService profileService;

    private final Faker faker;

    public OAuthDummyRunner(@Value("${runner.dummy.oauth.rows}") final long rows,
                            @Value("${runner.dummy.oauth.batch-size}") final long batchSize,
                            final OAuthService oAuthService,
                            final ProfileService profileService,
                            final Faker faker) {
        this.rows = rows;
        this.batchSize = batchSize;
        this.oAuthService = oAuthService;
        this.profileService = profileService;
        this.faker = faker;
    }

    @Override
    public void run(final String... args) {
        if (rows <= 0) {
            throw new IllegalStateException("OAuthDummyRunner requires at least 1 row");
        }

        if (batchSize <= 0) {
            throw new IllegalStateException("OAuthDummyRunner requires at least 1 batch size");
        }

        final long profileMinimumId = profileService.findProfileMinimumId();
        final List<OAuth> oAuths = new ArrayList<>();
        long row = 0L;

        while (true) {
            final Optional<Profile> profile = profileService.findWithOAuthById(profileMinimumId + row);

            if (profile.isPresent()) {
                final OAuth oAuth = OAuth.builder()
                        .profile(profile.get())
                        .identifier(generateUniqueIdentifier(row))
                        .provider(generateProvider())
                        .build();

                oAuths.add(oAuth);
                row++;
            }

            if (oAuths.size() >= batchSize || row >= rows || profile.isEmpty()) {
                try {
                    oAuthService.insertOAuths(oAuths);
                    log.info("Inserted dummy OAuths: {} rows", NumberFormat.getInstance().format(oAuths.size()));
                    oAuths.clear();
                } catch (final Exception ex) {
                    log.error("Failed to insert dummy OAuths: {}", ex.getMessage());
                }
            }

            if (row >= rows) {
                log.info("Finished dummy OAuths insertion: {} rows", NumberFormat.getInstance().format(row));
                break;
            }

            if (profile.isEmpty()) {
                log.info("Finished dummy OAuths insertion: Fewer Profiles than OAuths to insert: {} rows", NumberFormat.getInstance().format(row));
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