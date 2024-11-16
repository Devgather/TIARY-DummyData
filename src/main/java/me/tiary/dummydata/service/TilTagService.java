package me.tiary.dummydata.service;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.domain.TilTag;
import me.tiary.dummydata.repository.TilTagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TilTagService {
    private final TilTagRepository tilTagRepository;

    public void insertTilTags(final List<TilTag> tilTags) {
        tilTagRepository.saveAll(tilTags);
    }
}