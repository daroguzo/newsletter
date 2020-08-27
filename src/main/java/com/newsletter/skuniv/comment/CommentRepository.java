package com.newsletter.skuniv.comment;

import com.newsletter.skuniv.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
