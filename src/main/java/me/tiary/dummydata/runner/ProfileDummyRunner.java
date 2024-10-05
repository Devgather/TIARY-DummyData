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
@ConditionalOnProperty(prefix = "runner.dummy", name = "profile.enabled", matchIfMissing = true)
@Order(1)
@Slf4j
public class ProfileDummyRunner implements CommandLineRunner {
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
            throw new IllegalStateException("Rows for ProfileDummyRunner must be at least 1");
        }

        if (batchSize <= 0) {
            throw new IllegalStateException("Batch size for ProfileDummyRunner must be at least 1");
        }

        final List<Profile> profiles = new ArrayList<>();
        long row = 0L;

        while (true) {
            final Profile profile = Profile.builder()
                    .nickname(faker.expression("#{letterify '????????????????????'}"))
                    .picture(faker.internet().image())
                    .build();

            profiles.add(profile);
            row++;

            if (profiles.size() >= batchSize || row >= rows) {
                transactionTemplate.executeWithoutResult(status -> {
                    try {
                        profileRepository.saveAllAndFlush(profiles);
                        log.info("Insert Profile: {} rows", NumberFormat.getInstance().format(profiles.size()));
                        profiles.clear();
                    } catch (final Exception ex) {
                        log.error("Fail to insert Profile: {}", ex.getMessage());
                        status.setRollbackOnly();
                    }
                });

                if (row >= rows) {
                    log.info("Finish Profile insertion: {} rows", NumberFormat.getInstance().format(row));

                    break;
                }
            }
        }
    }
}