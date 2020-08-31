package com.newsletter.skuniv.post;

import com.newsletter.skuniv.domain.Post;
import com.newsletter.skuniv.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    boolean existsById(Long id);
    boolean existsByLikedUsers (User user);
    boolean existsBySharedUsers (User user);

    Post findByName (String name);
}
