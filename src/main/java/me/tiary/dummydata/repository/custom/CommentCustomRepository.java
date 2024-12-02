package me.tiary.dummydata.repository.custom;

import me.tiary.dummydata.domain.Comment;

import java.util.List;

public interface CommentCustomRepository {
    void saveBatch(final List<Comment> comments);
}