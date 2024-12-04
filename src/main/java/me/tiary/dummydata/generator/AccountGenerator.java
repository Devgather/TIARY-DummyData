package me.tiary.dummydata.generator;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.accessor.AccountAccessor;
import me.tiary.dummydata.annotation.EntityGenerationLogging;
import me.tiary.dummydata.annotation.EntityInsertionLogging;
import me.tiary.dummydata.domain.Account;
import me.tiary.dummydata.iterator.ProfileIterator;
import me.tiary.dummydata.iterator.factory.ProfileIteratorFactory;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AccountGenerator {
    private final AccountHandler accountHandler;

    private final ProfileIteratorFactory profileIteratorFactory;

    private final Faker faker;

    @Transactional
    @EntityGenerationLogging(entity = "Account")
    public long generateAccounts(final long rows, final long batchSize) {
        final List<Account> accounts = new ArrayList<>();
        final ProfileIterator profileIterator = profileIteratorFactory.create(batchSize);
        long totalRows;

        for (totalRows = 0L; totalRows < rows; totalRows++) {
            if (!profileIterator.hasNext()) {
                break;
            }

            final Account account = Account.builder()
                    .profile(profileIterator.next())
                    .email(generateUniqueEmail(Long.toString(totalRows)))
                    .password(generatePassword())
                    .build();

            accounts.add(account);

            if (accounts.size() >= batchSize) {
                accountHandler.insertAccounts(accounts);
                accounts.clear();
            }
        }

        if (!accounts.isEmpty()) {
            accountHandler.insertAccounts(accounts);
            accounts.clear();
        }

        return totalRows;
    }

    public String generateUniqueEmail(final String uniqueValue) {
        return faker.internet().emailAddress(faker.internet().username() + uniqueValue);
    }

    public String generatePassword() {
        return faker.internet().password();
    }

    @Component
    @RequiredArgsConstructor
    public static class AccountHandler {
        private final AccountAccessor accountAccessor;

        @Transactional(propagation = Propagation.REQUIRES_NEW)
        @EntityInsertionLogging(entity = "Account")
        public void insertAccounts(final List<Account> accounts) {
            accountAccessor.insertAccounts(accounts);
        }
    }
}