package me.tiary.dummydata.iterator.factory;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.accessor.ProfileAccessor;
import me.tiary.dummydata.iterator.ProfileRandomIterator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ProfileRandomIteratorFactory {
    private final ProfileAccessor profileAccessor;

    public ProfileRandomIterator create() {
        return new ProfileRandomIterator(profileAccessor);
    }

    public ProfileRandomIterator create(final long batchSize, final int maxFetchAttempts, final double duplicateProbability) {
        return new ProfileRandomIterator(profileAccessor, batchSize, maxFetchAttempts, duplicateProbability);
    }
}