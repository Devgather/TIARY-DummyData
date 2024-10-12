package me.tiary.dummydata.runner;

import lombok.extern.slf4j.Slf4j;
import me.tiary.dummydata.domain.Account;
import me.tiary.dummydata.domain.Profile;
import me.tiary.dummydata.repository.AccountRepository;
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
import java.util.Optional;

@Component
@ConditionalOnProperty(prefix = "runner.dummy.account", name = "enabled")
@Order(2)
@Slf4j
public final class AccountDummyRunner implements CommandLineRunner {
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
            throw new IllegalStateException("AccountDummyRunner requires at least 1 row");
        }

        if (batchSize <= 0) {
            throw new IllegalStateException("AccountDummyRunner requires at least 1 batch size");
        }

        final long profileMinimumId = findProfileMinimumId();
        final List<Account> accounts = new ArrayList<>();
        long row = 0L;

        while (true) {
            final Optional<Profile> profile = profileRepository.findById(profileMinimumId + row);

            if (profile.isPresent()) {
                final Account account = Account.builder()
                        .profile(profile.get())
                        .email(generateUniqueEmail(row))
                        .password(faker.internet().password())
                        .build();

                accounts.add(account);
                row++;
            }

            if (accounts.size() >= batchSize || row >= rows || profile.isEmpty()) {
                try {
                    insertAccounts(accounts);
                    log.info("Inserted dummy Accounts: {} rows", NumberFormat.getInstance().format(accounts.size()));
                    accounts.clear();
                } catch (final Exception ex) {
                    log.error("Failed to insert dummy Accounts: {}", ex.getMessage());
                }
            }

            if (row >= rows) {
                log.info("Finished dummy Accounts insertion: {} rows", NumberFormat.getInstance().format(row));
                break;
            }

            if (profile.isEmpty()) {
                log.info("Finished dummy Accounts insertion: Fewer Profiles than Accounts to insert: {} rows", NumberFormat.getInstance().format(row));
                break;
            }
        }
    }

    private String generateUniqueEmail(final long row) {
        return faker.internet().emailAddress(faker.internet().username() + row);
    }

    public long findProfileMinimumId() {
        final Optional<Profile> firstProfile = profileRepository.findFirstByOrderByIdAsc();

        if (firstProfile.isPresent()) {
            return firstProfile.get().getId();
        }

        return -1L;
    }

    public void insertAccounts(final List<Account> accounts) {
        transactionTemplate.executeWithoutResult(status -> {
            try {
                accountRepository.saveAllAndFlush(accounts);
            } catch (final Exception ex) {
                status.setRollbackOnly();
                throw ex;
            }
        });
    }
}