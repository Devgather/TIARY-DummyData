package me.tiary.dummydata.service;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.domain.Til;
import me.tiary.dummydata.repository.TilRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TilService {
    private final TransactionTemplate transactionTemplate;

    private final TilRepository tilRepository;

    public void insertTils(final List<Til> tils) {
        transactionTemplate.executeWithoutResult(status -> {
            try {
                tilRepository.saveAllAndFlush(tils);
            } catch (final Exception ex) {
                status.setRollbackOnly();
                throw ex;
            }
        });
    }
}