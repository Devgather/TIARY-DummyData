package me.tiary.dummydata.runner;

import me.tiary.dummydata.generator.AccountGenerator;
import org.springframework.boot.CommandLineRunner;

public final class AccountDummyRunner implements CommandLineRunner {
    private final long rows;

    private final long batchSize;

    private final AccountGenerator accountGenerator;

    public AccountDummyRunner(final long rows, final long batchSize, final AccountGenerator accountGenerator) {
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