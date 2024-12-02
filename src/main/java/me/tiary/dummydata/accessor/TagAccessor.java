package me.tiary.dummydata.accessor;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.data.Range;
import me.tiary.dummydata.domain.Tag;
import me.tiary.dummydata.repository.TagRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TagAccessor {
    private final TagRepository tagRepository;

    public Range findTagIdRange() {
        final Optional<Tag> firstTag = tagRepository.findFirstByOrderByIdAsc();
        final Optional<Tag> lastTag = tagRepository.findFirstByOrderByIdDesc();

        if (firstTag.isPresent() && lastTag.isPresent()) {
            return new Range(firstTag.get().getId(), lastTag.get().getId());
        }

        return new Range(-1L, -1L);
    }

    public Optional<Tag> findById(final long id) {
        return tagRepository.findById(id);
    }

    public void insertTags(final List<Tag> tags) {
        tagRepository.saveBatch(tags);
    }
}