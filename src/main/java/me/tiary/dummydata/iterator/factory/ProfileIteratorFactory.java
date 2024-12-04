package me.tiary.dummydata.iterator.factory;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.accessor.ProfileAccessor;
import me.tiary.dummydata.iterator.ProfileIterator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ProfileIteratorFactory {
    private final ProfileAccessor profileAccessor;

    public ProfileIterator create() {
        return new ProfileIterator(profileAccessor);
    }

    public ProfileIterator create(final long batchSize) {
        return new ProfileIterator(profileAccessor, batchSize);
    }
}