package me.tiary.dummydata.service;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.domain.TilTag;
import me.tiary.dummydata.repository.TilTagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TilTagService {
    private final TransactionTemplate transactionTemplate;

    private final TilTagRepository tilTagRepository;

    public void insertTilTags(final List<TilTag> tilTags) {
        transactionTemplate.executeWithoutResult(status -> {
            try {
                tilTagRepository.saveAllAndFlush(tilTags);
            } catch (final Exception ex) {
                status.setRollbackOnly();
                throw ex;
            }
        });
    }
}