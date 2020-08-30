package com.newsletter.skuniv.post;

import com.newsletter.skuniv.domain.Post;
import com.newsletter.skuniv.domain.User;
import com.newsletter.skuniv.user.UserRepository;
import com.newsletter.skuniv.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Controller
public class PostController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @PostConstruct
    public void init() {
        Post post1 = new Post();
        post1.setName("news1");
        postRepository.save(post1);
    }

    @GetMapping("/")
    public String index(Model model) {
        if (!userRepository.existsByIp(userService.getUserIp())) {
            User user = userService.saveNewUser();
            model.addAttribute(user);
        }
        model.addAttribute("ip", userService.getUserIp());
        return "index";
    }

    @GetMapping("/news1")
    public String news1(Model model) {
        Post post = postRepository.findByName("news1");

        if (!userRepository.existsByIp(userService.getUserIp())) {
            User user = userService.saveNewUser();
            post.addLikedUser(user);
            post.addSharedUser(user);
            model.addAttribute(user);

        }else {
            User user = userRepository.findByIp(userService.getUserIp());
            post.addLikedUser(user);
            post.addSharedUser(user);
            model.addAttribute(user);
        }
        model.addAttribute(post);
        return "news1";
    }

    @PostMapping("/news1")
    public String postNews1(RedirectAttributes redirectAttributes,
                            @RequestParam(required = false) String like, @RequestParam(required = false) String share) {
        Post post = postRepository.findByName("news1");
        User user = userRepository.findByIp(userService.getUserIp());
        if (like != null) {
            if (!postRepository.existsByLikedUsers(user)) {
                post.setLikeCount(post.getLikeCount() + 1);
                post.addLikedUser(user);
                postRepository.save(post);
                redirectAttributes.addFlashAttribute("message", "좋아요 누름");
            }else {
                post.setLikeCount(post.getLikeCount() - 1);
                post.deleteLikedUser(user);
                postRepository.save(post);
                redirectAttributes.addFlashAttribute("message", "좋아요 취소");
            }
        }

        if (share != null) {
            if (!postRepository.existsBySharedUsers(user)) {
                post.setShareCount(post.getShareCount() + 1);
                post.addSharedUser(user);
                postRepository.save(post);
                redirectAttributes.addFlashAttribute("message", "공유 누름");
            }else {
                post.setShareCount(post.getShareCount() - 1);
                post.deleteSharedUser(user);
                postRepository.save(post);
                redirectAttributes.addFlashAttribute("message", "공유 취소");
            }
        }
        return "redirect:/news1";
    }
}
