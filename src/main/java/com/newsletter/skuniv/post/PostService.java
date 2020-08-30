package com.newsletter.skuniv.post;

import com.newsletter.skuniv.domain.Post;
import com.newsletter.skuniv.user.UserRepository;
import com.newsletter.skuniv.user.UserService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Service
public class PostService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;



}
