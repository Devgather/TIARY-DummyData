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
public class TilAccessor {
    private final TilRepository tilRepository;

    public Range findTilIdRange() {
        final Optional<Til> firstTil = tilRepository.findFirstByOrderByIdAsc();
        final Optional<Til> lastTil = tilRepository.findFirstByOrderByIdDesc();

        if (firstTil.isPresent() && lastTil.isPresent()) {
            return new Range(firstTil.get().getId(), lastTil.get().getId());
        }

        return new Range(-1L, -1L);
    }

    public Optional<Til> findById(final long id) {
        return tilRepository.findById(id);
    }

    public void insertTils(final List<Til> tils) {
        tilRepository.saveAll(tils);
    }
}