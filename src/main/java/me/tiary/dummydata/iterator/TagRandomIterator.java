package me.tiary.dummydata.iterator;

import me.tiary.dummydata.accessor.TagAccessor;
import me.tiary.dummydata.data.Range;
import me.tiary.dummydata.domain.Tag;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class TagRandomIterator implements Iterator<Tag> {
    public static final long DEFAULT_BATCH_SIZE = 1L;

    public static final int DEFAULT_MAX_FETCH_ATTEMPTS = 3;

    private final TagAccessor tagAccessor;

    private final long batchSize;

    private final int maxFetchAttempts;

    private final Range tagIdRange;

    private final Queue<Tag> tagQueue;

    public TagRandomIterator(final TagAccessor tagAccessor) {
        this(tagAccessor, DEFAULT_BATCH_SIZE, DEFAULT_MAX_FETCH_ATTEMPTS);
    }

    public TagRandomIterator(final TagAccessor tagAccessor, final long batchSize, final int maxFetchAttempts) {
        if (batchSize <= 0) {
            throw new IllegalArgumentException("TagRandomIterator requires at least 1 batch size");
        }

        if (maxFetchAttempts <= 0) {
            throw new IllegalArgumentException("TagRandomIterator requires at least 1 fetch attempt");
        }

        this.tagAccessor = tagAccessor;
        this.batchSize = batchSize;
        this.maxFetchAttempts = maxFetchAttempts;
        this.tagIdRange = tagAccessor.findTagIdRange();
        this.tagQueue = new ArrayDeque<>();
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
            final List<Long> ids = tagIdRange.generateRandomValues(batchSize);

            final Map<Long, Tag> tagMap = tagAccessor.findAllById(ids).stream()
                    .collect(Collectors.toMap(Tag::getId, Function.identity()));

            ids.stream()
                    .map(tagMap::get)
                    .filter(Objects::nonNull)
                    .forEach(tagQueue::add);
        }
    }
}