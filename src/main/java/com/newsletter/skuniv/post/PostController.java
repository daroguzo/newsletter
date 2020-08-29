package com.newsletter.skuniv.post;

import com.newsletter.skuniv.domain.Post;
import com.newsletter.skuniv.domain.User;
import com.newsletter.skuniv.user.UserRepository;
import com.newsletter.skuniv.user.UserService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Controller
public class PostController {

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/")
    public String index(Model model) {
        if(!userRepository.existsByIp(userService.getUserIp())) {
            User user = userService.saveNewUser();
            model.addAttribute(user);
        }
        return "index";
    }

    @GetMapping("/news1")
    public String news1(Model model) {
        //model.addAttribute("ip", getUserIp());
        return "news1";
    }
}
