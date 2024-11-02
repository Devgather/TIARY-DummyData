package me.tiary.dummydata.runner;

import me.tiary.dummydata.generator.AccountGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "runner.dummy.account", name = "enabled")
@Order(2)
public final class AccountDummyRunner implements CommandLineRunner {
    private final long rows;

    private final long batchSize;

    private final AccountGenerator accountGenerator;

    public AccountDummyRunner(@Value("${runner.dummy.account.rows}") final long rows,
                              @Value("${runner.dummy.account.batch-size}") final long batchSize,
                              final AccountGenerator accountGenerator) {
        if (rows <= 0) {
            throw new IllegalArgumentException("AccountDummyRunner requires at least 1 row");
        }

        if (batchSize <= 0) {
            throw new IllegalArgumentException("AccountDummyRunner requires at least 1 batch size");
        }

        this.rows = rows;
        this.batchSize = batchSize;
        this.accountGenerator = accountGenerator;
    }

    @Override
    public void run(final String... args) {
        accountGenerator.generateAccounts(rows, batchSize);
    }
}