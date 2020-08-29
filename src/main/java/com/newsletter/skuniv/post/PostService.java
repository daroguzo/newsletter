package com.newsletter.skuniv.post;

import com.newsletter.skuniv.user.UserRepository;
import com.newsletter.skuniv.user.UserService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@NoArgsConstructor
@Service
public class PostService {

    private UserService userService;
    private UserRepository userRepository;


}
