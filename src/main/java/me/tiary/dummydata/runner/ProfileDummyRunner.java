package me.tiary.dummydata.runner;

import lombok.extern.slf4j.Slf4j;
import me.tiary.dummydata.domain.Profile;
import me.tiary.dummydata.repository.ProfileRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

@Component
@ConditionalOnProperty(prefix = "runner.dummy.profile", name = "enabled")
@Order(1)
@Slf4j
public final class ProfileDummyRunner implements CommandLineRunner {
    private final TransactionTemplate transactionTemplate;

    private final ProfileRepository profileRepository;

    private final long rows;

    private final long batchSize;

    private final Faker faker;

    public ProfileDummyRunner(final TransactionTemplate transactionTemplate,
                              final ProfileRepository profileRepository,
                              @Value("${runner.dummy.profile.rows}") final long rows,
                              @Value("${runner.dummy.profile.batch-size}") final long batchSize,
                              final Faker faker) {
        this.transactionTemplate = transactionTemplate;
        this.profileRepository = profileRepository;
        this.rows = rows;
        this.batchSize = batchSize;
        this.faker = faker;
    }

    @Override
    public void run(final String... args) {
        if (rows <= 0) {
            throw new IllegalStateException("ProfileDummyRunner requires at least 1 row");
        }

        if (batchSize <= 0) {
            throw new IllegalStateException("ProfileDummyRunner requires at least 1 batch size");
        }

        final List<Profile> profiles = new ArrayList<>();
        long row = 0L;

        while (true) {
            final Profile profile = Profile.builder()
                    .nickname(generateUniqueNickname(row))
                    .picture(faker.internet().image())
                    .build();

            profiles.add(profile);
            row++;

            if (profiles.size() >= batchSize || row >= rows) {
                try {
                    insertProfiles(profiles);
                    log.info("Inserted dummy Profiles: {} rows", NumberFormat.getInstance().format(profiles.size()));
                    profiles.clear();
                } catch (final Exception ex) {
                    log.error("Failed to insert dummy Profiles: {}", ex.getMessage());
                }
            }

            if (row >= rows) {
                log.info("Finished dummy Profiles insertion: {} rows", NumberFormat.getInstance().format(row));
                break;
            }
        }
    }

    private String generateUniqueNickname(final long row) {
        final int rowDigits = Long.toString(row).length();

        return faker.expression("#{letterify '" + "?".repeat(Profile.NICKNAME_MAX_LENGTH - rowDigits) + "'}" + row);
    }

    public void insertProfiles(final List<Profile> profiles) {
        transactionTemplate.executeWithoutResult(status -> {
            try {
                profileRepository.saveAllAndFlush(profiles);
            } catch (final Exception ex) {
                status.setRollbackOnly();
                throw ex;
            }
        });
    }
}