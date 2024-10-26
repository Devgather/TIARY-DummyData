package me.tiary.dummydata.service;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.domain.Tag;
import me.tiary.dummydata.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TransactionTemplate transactionTemplate;

    private final TagRepository tagRepository;

    public long findTagMinimumId() {
        final Optional<Tag> firstTag = tagRepository.findFirstByOrderByIdAsc();

        if (firstTag.isPresent()) {
            return firstTag.get().getId();
        }

        return -1L;
    }

    public long findTagMaximumId() {
        final Optional<Tag> lastTag = tagRepository.findFirstByOrderByIdDesc();

        if (lastTag.isPresent()) {
            return lastTag.get().getId();
        }

        return -1L;
    }

    public Optional<Tag> findById(final long id) {
        return tagRepository.findById(id);
    }

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