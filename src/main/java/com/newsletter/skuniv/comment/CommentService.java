package com.newsletter.skuniv.comment;

import com.newsletter.skuniv.domain.Comment;
import com.newsletter.skuniv.domain.Post;
import com.newsletter.skuniv.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class CommentService {


    private final CommentRepository commentRepository;

    public Comment saveNewComment(@Valid CommentForm commentForm, Post post, User user) {
        Comment comment = Comment.builder()
                .content(commentForm.getContent())
                .created(LocalDateTime.now())
                .post(post)
                .user(user)
                .build();
        return commentRepository.save(comment);
    }


}