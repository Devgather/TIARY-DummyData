package me.tiary.dummydata.iterator;

import me.tiary.dummydata.accessor.ProfileAccessor;
import me.tiary.dummydata.data.Range;
import me.tiary.dummydata.domain.Profile;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public final class ProfileIterator implements Iterator<Profile> {
    private final ProfileAccessor profileAccessor;

    private final long batchSize;

    private final Range profileIdRange;

    private final Queue<Profile> profileQueue;

    private Range nextBatchIdRange;

    public ProfileIterator(final ProfileAccessor profileAccessor) {
        this(profileAccessor, 1L);
    }

    public ProfileIterator(final ProfileAccessor profileAccessor, final long batchSize) {
        if (batchSize <= 0) {
            throw new IllegalArgumentException("ProfileIterator requires at least 1 batch size");
        }

        this.profileAccessor = profileAccessor;
        this.batchSize = batchSize;
        this.profileIdRange = profileAccessor.findProfileIdRange();
        this.profileQueue = new ArrayDeque<>();
        this.nextBatchIdRange = calculateNextBatchIdRange(profileIdRange.lowerBound() - 1);
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
            throw new NoSuchElementException("ProfileIterator has no more elements");
        }

        return profileQueue.poll();
    }

    private void fetchNextBatch() {
        while (profileQueue.isEmpty() && nextBatchIdRange.lowerBound() <= profileIdRange.upperBound()) {
            profileQueue.addAll(profileAccessor.findAllByIdBetween(nextBatchIdRange));
            nextBatchIdRange = calculateNextBatchIdRange(nextBatchIdRange.upperBound());
        }
    }

    private Range calculateNextBatchIdRange(final long previousBatchUpperBoundId) {
        return new Range(previousBatchUpperBoundId + 1, Math.min(previousBatchUpperBoundId + batchSize, profileIdRange.upperBound()));
    }
}