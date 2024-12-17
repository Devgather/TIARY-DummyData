package me.tiary.dummydata.iterator.factory;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.accessor.TagAccessor;
import me.tiary.dummydata.iterator.TagRandomIterator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class TagRandomIteratorFactory {
    private final TagAccessor tagAccessor;

    public TagRandomIterator create() {
        return new TagRandomIterator(tagAccessor);
    }

    public TagRandomIterator create(final long batchSize, final int maxFetchAttempts) {
        return new TagRandomIterator(tagAccessor, batchSize, maxFetchAttempts);
    }
}