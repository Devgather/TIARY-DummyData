package me.tiary.dummydata.runner;

import lombok.extern.slf4j.Slf4j;
import me.tiary.dummydata.data.Range;
import me.tiary.dummydata.domain.Profile;
import me.tiary.dummydata.domain.Til;
import me.tiary.dummydata.service.ProfileService;
import me.tiary.dummydata.service.TilService;
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
@ConditionalOnProperty(prefix = "runner.dummy.til", name = "enabled")
@Order(5)
@Slf4j
public final class TilDummyRunner implements CommandLineRunner {
    private final Range rowsRangePerProfile;

    private final long batchSize;

    private final TilService tilService;

    private final ProfileService profileService;

    private final Faker faker;

    public TilDummyRunner(@Value("${runner.dummy.til.rows-range-per-profile}") final Range rowsRangePerProfile,
                          @Value("${runner.dummy.til.batch-size}") final long batchSize,
                          final TilService tilService,
                          final ProfileService profileService,
                          final Faker faker) {
        if (batchSize <= 0) {
            throw new IllegalArgumentException("TilDummyRunner requires at least 1 batch size");
        }

        this.rowsRangePerProfile = rowsRangePerProfile;
        this.batchSize = batchSize;
        this.tilService = tilService;
        this.profileService = profileService;
        this.faker = faker;
    }

    @Override
    public void run(final String... args) throws Exception {
        final long profileMinimumId = profileService.findProfileMinimumId();
        final List<Til> tils = new ArrayList<>();
        long profileRow = 0L;
        long tilRow = 0L;

        while (true) {
            final Optional<Profile> profile = profileService.findWithTilById(profileMinimumId + profileRow++);

            if (profile.isPresent()) {
                final long tilRows = rowsRangePerProfile.generateRandomValue();

                for (long row = 0L; row < tilRows; row++) {
                    final Til til = Til.builder()
                            .profile(profile.get())
                            .title(faker.lorem().sentence())
                            .content(faker.lorem().paragraph())
                            .build();

                    tils.add(til);
                    tilRow++;

                    if (tils.size() >= batchSize) {
                        try {
                            tilService.insertTils(tils);
                            log.info("Inserted dummy Tils: {} rows", NumberFormat.getInstance().format(tils.size()));
                            tils.clear();
                        } catch (final Exception ex) {
                            log.error("Failed to insert dummy Tils: {}", ex.getMessage());
                        }
                    }
                }
            }

            if (profile.isEmpty() && !tils.isEmpty()) {
                try {
                    tilService.insertTils(tils);
                    log.info("Inserted dummy Tils: {} rows", NumberFormat.getInstance().format(tils.size()));
                    tils.clear();
                } catch (final Exception ex) {
                    log.error("Failed to insert dummy Tils: {}", ex.getMessage());
                }
            }

            if (profile.isEmpty()) {
                log.info("Finished dummy Tils insertion: {} rows", NumberFormat.getInstance().format(tilRow));
                break;
            }
        }
    }
}