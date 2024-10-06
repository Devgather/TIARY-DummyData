package me.tiary.dummydata.runner;

import lombok.extern.slf4j.Slf4j;
import me.tiary.dummydata.domain.Account;
import me.tiary.dummydata.domain.Profile;
import me.tiary.dummydata.repository.AccountRepository;
import me.tiary.dummydata.repository.ProfileRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@ConditionalOnExpression("${runner.dummy.profile.enabled} and ${runner.dummy.account.enabled}")
@Order(2)
@Slf4j
public class AccountDummyRunner implements CommandLineRunner {
    private final TransactionTemplate transactionTemplate;

    private final AccountRepository accountRepository;

    private final ProfileRepository profileRepository;

    private final long rows;

    private final long batchSize;

    private final Faker faker;

    public AccountDummyRunner(final TransactionTemplate transactionTemplate,
                              final AccountRepository accountRepository,
                              final ProfileRepository profileRepository,
                              @Value("${runner.dummy.account.rows}") final long rows,
                              @Value("${runner.dummy.account.batch-size}") final long batchSize,
                              final Faker faker) {
        this.transactionTemplate = transactionTemplate;
        this.accountRepository = accountRepository;
        this.profileRepository = profileRepository;
        this.rows = rows;
        this.batchSize = batchSize;
        this.faker = faker;
    }

    @Override
    public void run(final String... args) {
        if (rows <= 0) {
            throw new IllegalStateException("Rows for AccountDummyRunner must be at least 1");
        }

        if (batchSize <= 0) {
            throw new IllegalStateException("Batch size for AccountDummyRunner must be at least 1");
        }

        final long minimumProfileId = profileRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new IllegalStateException("Profile number for AccountDummyRunner must be at least 1"))
                .getId();

        final List<Account> accounts = new ArrayList<>();
        long row = 0L;

        while (true) {
            final Optional<Profile> profile = profileRepository.findById(minimumProfileId + row);

            if (profile.isPresent()) {
                final Account account = Account.builder()
                        .profile(profile.get())
                        .email(faker.internet().emailAddress())
                        .password(faker.internet().password())
                        .build();

                accounts.add(account);
                row++;
            }

            if (accounts.size() >= batchSize || row >= rows || profile.isEmpty()) {
                transactionTemplate.executeWithoutResult(status -> {
                    try {
                        accountRepository.saveAllAndFlush(accounts);
                        log.info("Insert Account: {} rows", NumberFormat.getInstance().format(accounts.size()));
                        accounts.clear();
                    } catch (final Exception ex) {
                        log.error("Fail to insert Account: {}", ex.getMessage());
                        status.setRollbackOnly();
                    }
                });

                if (row >= rows) {
                    log.info("Finish Account insertion: {} rows", NumberFormat.getInstance().format(row));

                    break;
                }

                if (profile.isEmpty()) {
                    log.info("Finish Account insertion because there are fewer Profiles than Accounts to insert: {} rows", NumberFormat.getInstance().format(row));

                    break;
                }
            }
        }
    }
}