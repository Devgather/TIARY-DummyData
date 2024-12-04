package me.tiary.dummydata.iterator;

import me.tiary.dummydata.accessor.TilAccessor;
import me.tiary.dummydata.data.Range;
import me.tiary.dummydata.domain.Til;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public final class TilIterator implements Iterator<Til> {
    private final TilAccessor tilAccessor;

    private final long batchSize;

    private final Range tilIdRange;

    private final Queue<Til> tilQueue;

    private Range nextBatchIdRange;

    public TilIterator(final TilAccessor tilAccessor) {
        this(tilAccessor, 1L);
    }

    public TilIterator(final TilAccessor tilAccessor, final long batchSize) {
        if (batchSize <= 0) {
            throw new IllegalArgumentException("TilIterator requires at least 1 batch size");
        }

        this.tilAccessor = tilAccessor;
        this.batchSize = batchSize;
        this.tilIdRange = tilAccessor.findTilIdRange();
        this.tilQueue = new ArrayDeque<>();
        this.nextBatchIdRange = calculateNextBatchIdRange(tilIdRange.lowerBound() - 1);
    }

    @Override
    public boolean hasNext() {
        if (tilQueue.isEmpty()) {
            fetchNextBatch();
        }

        return !tilQueue.isEmpty();
    }

    @Override
    public Til next() {
        if (!hasNext()) {
            throw new NoSuchElementException("TilIterator has no more elements");
        }

        return tilQueue.poll();
    }

    private void fetchNextBatch() {
        while (tilQueue.isEmpty() && nextBatchIdRange.lowerBound() <= tilIdRange.upperBound()) {
            tilQueue.addAll(tilAccessor.findAllByIdBetween(nextBatchIdRange));
            nextBatchIdRange = calculateNextBatchIdRange(nextBatchIdRange.upperBound());
        }
    }

    private Range calculateNextBatchIdRange(final long previousBatchUpperBoundId) {
        return new Range(previousBatchUpperBoundId + 1, Math.min(previousBatchUpperBoundId + batchSize, tilIdRange.upperBound()));
    }
}