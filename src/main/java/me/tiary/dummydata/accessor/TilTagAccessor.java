package me.tiary.dummydata.accessor;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.domain.TilTag;
import me.tiary.dummydata.repository.TilTagRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public final class TilTagAccessor {
    private final TilTagRepository tilTagRepository;

    public void insertTilTags(final List<TilTag> tilTags) {
        tilTagRepository.saveBatch(tilTags);
    }
}