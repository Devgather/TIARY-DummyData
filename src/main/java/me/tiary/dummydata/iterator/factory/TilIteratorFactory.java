package me.tiary.dummydata.iterator.factory;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.accessor.TilAccessor;
import me.tiary.dummydata.iterator.TilIterator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class TilIteratorFactory {
    private final TilAccessor tilAccessor;

    public TilIterator create() {
        return new TilIterator(tilAccessor);
    }

    public TilIterator create(final long batchSize) {
        return new TilIterator(tilAccessor, batchSize);
    }
}