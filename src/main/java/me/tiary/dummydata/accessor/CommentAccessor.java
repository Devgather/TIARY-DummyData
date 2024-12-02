package me.tiary.dummydata.accessor;

import lombok.RequiredArgsConstructor;
import me.tiary.dummydata.domain.Comment;
import me.tiary.dummydata.repository.CommentRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentAccessor {
    private final CommentRepository commentRepository;

    public void insertComments(final List<Comment> comments) {
        commentRepository.saveBatch(comments);
    }
}