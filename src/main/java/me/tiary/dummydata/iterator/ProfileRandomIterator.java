package me.tiary.dummydata.iterator;

import me.tiary.dummydata.accessor.ProfileAccessor;
import me.tiary.dummydata.data.Range;
import me.tiary.dummydata.domain.Profile;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

public final class ProfileRandomIterator implements Iterator<Profile> {
    public static final long DEFAULT_BATCH_SIZE = 1L;

    public static final int DEFAULT_MAX_FETCH_ATTEMPTS = 3;

    public static final double DEFAULT_DUPLICATE_PROBABILITY = 0.0;

    private static final Random random;

    private final ProfileAccessor profileAccessor;

    private final long batchSize;

    private final int maxFetchAttempts;

    private final double duplicateProbability;

    private final Range profileIdRange;

    private final Queue<Profile> profileQueue;

    private Range nextBatchIdRange;

    static {
        try {
            random = SecureRandom.getInstanceStrong();
        } catch (final NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Failed to initialize ProfileRandomIterator", ex);
        }
    }

    public ProfileRandomIterator(final ProfileAccessor profileAccessor) {
        this(profileAccessor, DEFAULT_BATCH_SIZE, DEFAULT_MAX_FETCH_ATTEMPTS, DEFAULT_DUPLICATE_PROBABILITY);
    }

    public ProfileRandomIterator(final ProfileAccessor profileAccessor, final long batchSize, final int maxFetchAttempts, final double duplicateProbability) {
        if (batchSize <= 0) {
            throw new IllegalArgumentException("ProfileRandomIterator requires at least 1 batch size");
        }

        if (maxFetchAttempts <= 0) {
            throw new IllegalArgumentException("ProfileRandomIterator requires at least 1 fetch attempt");
        }

        if (duplicateProbability < 0.0 || duplicateProbability > 1.0) {
            throw new IllegalArgumentException("ProfileRandomIterator requires duplicate probability between 0.0 and 1.0");
        }

        this.profileAccessor = profileAccessor;
        this.batchSize = batchSize;
        this.maxFetchAttempts = maxFetchAttempts;
        this.duplicateProbability = duplicateProbability;
        this.profileIdRange = profileAccessor.findProfileIdRange();
        this.profileQueue = new ArrayDeque<>();
        this.nextBatchIdRange = calculateNextBatchIdRange();
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
            final List<Profile> profiles = profileAccessor.findAllByIdBetween(nextBatchIdRange);

            for (int index = 0; index < profiles.size(); index++) {
                if (random.nextDouble() < duplicateProbability) {
                    profiles.set(index, profiles.get(random.nextInt(profiles.size())));
                }
            }

            Collections.shuffle(profiles, random);
            profileQueue.addAll(profiles);
            nextBatchIdRange = calculateNextBatchIdRange();
        }
    }

    private Range calculateNextBatchIdRange() {
        final long pivotId = profileIdRange.generateRandomValue();

        return new Range(
                Math.min(pivotId, profileIdRange.upperBound() - batchSize + 1),
                Math.min(pivotId + batchSize - 1, profileIdRange.upperBound())
        );
    }
}