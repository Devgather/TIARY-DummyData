package me.tiary.dummydata.iterator;

import me.tiary.dummydata.accessor.TagAccessor;
import me.tiary.dummydata.data.Range;
import me.tiary.dummydata.domain.Tag;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

public final class TagRandomIterator implements Iterator<Tag> {
    public static final long DEFAULT_BATCH_SIZE = 1L;

    public static final int DEFAULT_MAX_FETCH_ATTEMPTS = 3;

    public static final double DEFAULT_DUPLICATE_PROBABILITY = 0.0;

    private static final Random random;

    private final TagAccessor tagAccessor;

    private final long batchSize;

    private final int maxFetchAttempts;

    private final double duplicateProbability;

    private final Range tagIdRange;

    private final Queue<Tag> tagQueue;

    private Range nextBatchIdRange;

    static {
        try {
            random = SecureRandom.getInstanceStrong();
        } catch (final NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Failed to initialize TagRandomIterator", ex);
        }
    }

    public TagRandomIterator(final TagAccessor tagAccessor) {
        this(tagAccessor, DEFAULT_BATCH_SIZE, DEFAULT_MAX_FETCH_ATTEMPTS, DEFAULT_DUPLICATE_PROBABILITY);
    }

    public TagRandomIterator(final TagAccessor tagAccessor, final long batchSize, final int maxFetchAttempts, final double duplicateProbability) {
        if (batchSize <= 0) {
            throw new IllegalArgumentException("TagRandomIterator requires at least 1 batch size");
        }

        if (maxFetchAttempts <= 0) {
            throw new IllegalArgumentException("TagRandomIterator requires at least 1 fetch attempt");
        }

        if (duplicateProbability < 0.0 || duplicateProbability > 1.0) {
            throw new IllegalArgumentException("TagRandomIterator requires duplicate probability between 0.0 and 1.0");
        }

        this.tagAccessor = tagAccessor;
        this.batchSize = batchSize;
        this.maxFetchAttempts = maxFetchAttempts;
        this.duplicateProbability = duplicateProbability;
        this.tagIdRange = tagAccessor.findTagIdRange();
        this.tagQueue = new ArrayDeque<>();
        this.nextBatchIdRange = calculateNextBatchIdRange();
    }

    @Override
    public boolean hasNext() {
        if (tagQueue.isEmpty()) {
            fetchNextBatch();
        }

        return !tagQueue.isEmpty();
    }

    @Override
    public Tag next() {
        if (!hasNext()) {
            throw new NoSuchElementException("TagRandomIterator has no more elements");
        }

        return tagQueue.poll();
    }

    private void fetchNextBatch() {
        for (int attempt = 0; attempt < maxFetchAttempts && tagQueue.isEmpty(); attempt++) {
            final List<Tag> tags = tagAccessor.findAllByIdBetween(nextBatchIdRange);

            for (int index = 0; index < tags.size(); index++) {
                if (random.nextDouble() < duplicateProbability) {
                    tags.set(index, tags.get(random.nextInt(tags.size())));
                }
            }

            Collections.shuffle(tags, random);
            tagQueue.addAll(tags);
            nextBatchIdRange = calculateNextBatchIdRange();
        }
    }

    private Range calculateNextBatchIdRange() {
        final long pivotId = tagIdRange.generateRandomValue();

        return new Range(
                Math.min(pivotId, tagIdRange.upperBound() - batchSize + 1),
                Math.min(pivotId + batchSize - 1, tagIdRange.upperBound())
        );
    }
}