package me.tiary.dummydata.iterator;

import me.tiary.dummydata.accessor.ProfileAccessor;
import me.tiary.dummydata.data.Range;
import me.tiary.dummydata.domain.Profile;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ProfileRandomIterator implements Iterator<Profile> {
    public static final long DEFAULT_BATCH_SIZE = 1L;

    public static final int DEFAULT_MAX_FETCH_ATTEMPTS = 3;

    private final ProfileAccessor profileAccessor;

    private final long batchSize;

    private final int maxFetchAttempts;

    private final Range profileIdRange;

    private final Queue<Profile> profileQueue;

    public ProfileRandomIterator(final ProfileAccessor profileAccessor) {
        this(profileAccessor, DEFAULT_BATCH_SIZE, DEFAULT_MAX_FETCH_ATTEMPTS);
    }

    public ProfileRandomIterator(final ProfileAccessor profileAccessor, final long batchSize, final int maxFetchAttempts) {
        if (batchSize <= 0) {
            throw new IllegalArgumentException("ProfileRandomIterator requires at least 1 batch size");
        }

        if (maxFetchAttempts <= 0) {
            throw new IllegalArgumentException("ProfileRandomIterator requires at least 1 fetch attempt");
        }

        this.profileAccessor = profileAccessor;
        this.batchSize = batchSize;
        this.maxFetchAttempts = maxFetchAttempts;
        this.profileIdRange = profileAccessor.findProfileIdRange();
        this.profileQueue = new ArrayDeque<>();
    }

    @Override
    public boolean hasNext() {
        if (profileQueue.isEmpty()) {
            fetchNextBatch();
        }

        return !profileQueue.isEmpty();
    }

    @Override
    public Profile next() {
        if (!hasNext()) {
            throw new NoSuchElementException("ProfileRandomIterator has no more elements");
        }

        return profileQueue.poll();
    }

    private void fetchNextBatch() {
        for (int attempt = 0; attempt < maxFetchAttempts && profileQueue.isEmpty(); attempt++) {
            final List<Long> ids = profileIdRange.generateRandomValues(batchSize);

            final Map<Long, Profile> profileMap = profileAccessor.findAllById(ids).stream()
                    .collect(Collectors.toMap(Profile::getId, Function.identity()));

            ids.stream()
                    .map(profileMap::get)
                    .filter(Objects::nonNull)
                    .forEach(profileQueue::add);
        }
    }
}