package me.tiary.dummydata.iterator;

import me.tiary.dummydata.data.Range;
import me.tiary.dummydata.domain.Til;
import me.tiary.dummydata.service.TilService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.NoSuchElementException;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public final class TilIterator implements Iterator<Til> {
    private final TilService tilService;

    private final Range tilIdRange;

    private Til currentTil;

    public TilIterator(final TilService tilService) {
        this.tilService = tilService;
        this.tilIdRange = tilService.findTilIdRange();
        this.currentTil = findNext(tilIdRange.lowerBound() - 1);
    }

    @Override
    public boolean hasNext() {
        return currentTil != null;
    }

    @Override
    public Til next() {
        if (!hasNext()) {
            throw new NoSuchElementException("TilIterator has no more elements");
        }

        final Til result = currentTil;
        currentTil = findNext(result.getId());

        return result;
    }

    private Til findNext(final long currentId) {
        Til next = null;
        long nextId = currentId + 1;

        while (next == null && nextId <= tilIdRange.upperBound()) {
            next = tilService.findById(nextId).orElse(null);
            nextId++;
        }

        return next;
    }
}