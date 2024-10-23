package me.tiary.dummydata.service;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.domain.Tag;
import me.tiary.dummydata.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TransactionTemplate transactionTemplate;

    private final TagRepository tagRepository;

    public void insertTags(final List<Tag> tags) {
        transactionTemplate.executeWithoutResult(status -> {
            try {
                tagRepository.saveAllAndFlush(tags);
            } catch (final Exception ex) {
                status.setRollbackOnly();
                throw ex;
            }
        });
    }
}