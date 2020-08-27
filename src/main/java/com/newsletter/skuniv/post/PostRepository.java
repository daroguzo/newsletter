package com.newsletter.skuniv.post;

import com.newsletter.skuniv.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
