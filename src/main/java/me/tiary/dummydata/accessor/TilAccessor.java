package me.tiary.dummydata.accessor;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.data.Range;
import me.tiary.dummydata.domain.Til;
import me.tiary.dummydata.repository.TilRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public final class TilAccessor {
    private final TilRepository tilRepository;

    public Range findTilIdRange() {
        final Optional<Til> firstTil = tilRepository.findFirstByOrderByIdAsc();
        final Optional<Til> lastTil = tilRepository.findFirstByOrderByIdDesc();

        if (firstTil.isPresent() && lastTil.isPresent()) {
            return new Range(firstTil.get().getId(), lastTil.get().getId());
        }

        return new Range(-1L, -1L);
    }

    public List<Til> findAllByIdBetween(final long lowerBoundId, final long upperBoundId) {
        return tilRepository.findAllByIdBetween(lowerBoundId, upperBoundId);
    }

    public List<Til> findAllByIdBetween(final Range idRange) {
        return findAllByIdBetween(idRange.lowerBound(), idRange.upperBound());
    }

    public void insertTils(final List<Til> tils) {
        tilRepository.saveBatch(tils);
    }
}