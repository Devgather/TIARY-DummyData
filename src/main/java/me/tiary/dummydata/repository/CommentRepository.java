package me.tiary.dummydata.repository;

import me.tiary.dummydata.domain.Comment;
import me.tiary.dummydata.repository.custom.CommentCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentCustomRepository {
}