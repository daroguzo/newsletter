package com.newsletter.skuniv.post;

import com.newsletter.skuniv.comment.CommentForm;
import com.newsletter.skuniv.comment.CommentRepository;
import com.newsletter.skuniv.comment.CommentService;
import com.newsletter.skuniv.domain.Comment;
import com.newsletter.skuniv.domain.Post;
import com.newsletter.skuniv.domain.User;
import com.newsletter.skuniv.user.UserRepository;
import com.newsletter.skuniv.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class PostController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentService commentService;
    private final CommentRepository commentRepository;

    @PostConstruct
    public void init() {
        Post post1 = new Post();
        post1.setName("news1");

        Post post2 = new Post();
        post2.setName("news2");

        Post post3 = new Post();
        post3.setName("news3");

        postRepository.save(post1);
        postRepository.save(post2);

        postRepository.save(post3);
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

    @GetMapping("/home")
    public String home(Model model) {
        Post post1 = postRepository.findByName("news2");
        Post post2 = postRepository.findByName("news3");

        List<Comment> comments1 = post1.getComments();
        List<Comment> comments2 = post2.getComments();

        if (!userRepository.existsByIp(userService.getUserIp())) {
            User user = userService.saveNewUser();
            model.addAttribute(user);

        } else {
            User user = userRepository.findByIp(userService.getUserIp());
            model.addAttribute(user);
        }
        model.addAttribute("post1", post1);
        model.addAttribute("post2", post2);
        model.addAttribute(new CommentForm());
        model.addAttribute("comments1", comments1);
        model.addAttribute("comments2", comments2);

        return "home";
    }

    @PostMapping("/home")
    public String homePost(@RequestParam(required = false) String like1, @RequestParam(required = false) String share1,
                           @RequestParam(required = false) String like2, @RequestParam(required = false) String share2,
                           @Valid CommentForm commentForm) {
        Post post = postRepository.findByName("news2");
        Post post2 = postRepository.findByName("news3");
        User user = userRepository.findByIp(userService.getUserIp());
        if (like1 != null) {
            if (!post.getLikedUsers().contains(user)) {
                post.getLikedUsers().add(user);
                postRepository.save(post);
            } else {
                post.getLikedUsers().remove(user);
                postRepository.save(post);
            }
        }

        if (like2 != null) {
            if (!post2.getLikedUsers().contains(user)) {
                post2.addLikedUser(user);
                postRepository.save(post2);
            } else {
                post2.deleteLikedUser(user);
                postRepository.save(post2);
            }
        }

        if (share1 != null) {
            if (!post.getSharedUsers().contains(user)) {
                post.getSharedUsers().add(user);
                postRepository.save(post);
            }
        }

        if (share2 != null) {
            if (!post2.getSharedUsers().contains(user)) {
                post2.getSharedUsers().add(user);
                postRepository.save(post2);
            }
        }

        if (commentForm.getContent() != null) {
            Comment comment = commentService.saveNewComment(commentForm, post, user);
            post.addComment(comment);
            user.addComment(comment);
        }
        return "redirect:/home";
    }


    @GetMapping("/news1")
    public String news1(Model model) {
        Post post = postRepository.findByName("news1");
        List<Comment> comments = post.getComments();

        if (!userRepository.existsByIp(userService.getUserIp())) {
            User user = userService.saveNewUser();
            post.addLikedUser(user);
            post.addSharedUser(user);
            model.addAttribute(user);

        } else {
            User user = userRepository.findByIp(userService.getUserIp());
            post.addLikedUser(user);
            post.addSharedUser(user);
            model.addAttribute(user);
        }
        model.addAttribute(post);
        model.addAttribute(new CommentForm());
        model.addAttribute("commentList", comments);
        return "news1";
    }

    @PostMapping("/news1")
    public String postNews1(RedirectAttributes redirectAttributes,
                            @RequestParam(required = false) String like, @RequestParam(required = false) String share,
                            @RequestParam(required = false) Long commentId,
                            @Valid CommentForm commentForm, Errors errors) {
        if (errors.hasErrors()) {
            redirectAttributes.addFlashAttribute("contentError", "댓글 길이를 준수해주세요.");
            return "redirect:/news1";
        }
        Post post = postRepository.findByName("news1");
        User user = userRepository.findByIp(userService.getUserIp());
        if (like != null) {
            if (!postRepository.existsByLikedUsers(user)) {
                post.addLikedUser(user);
                postRepository.save(post);
                redirectAttributes.addFlashAttribute("message", "좋아요 누름");
            } else {
                post.deleteLikedUser(user);
                postRepository.save(post);
                redirectAttributes.addFlashAttribute("message", "좋아요 취소");
            }
        }

        if (share != null) {
            if (!postRepository.existsBySharedUsers(user)) {
                post.addSharedUser(user);
                postRepository.save(post);
                redirectAttributes.addFlashAttribute("message", "공유 누름");
            } else {
                redirectAttributes.addFlashAttribute("message", "공유는 한번만 할 수 있습니다.");
            }
        }

        if (commentForm.getContent() != null) {
            Comment comment = commentService.saveNewComment(commentForm, post, user);
            post.addComment(comment);
            user.addComment(comment);
        }

        if (commentId != null) {
            Optional<Comment> optionalComment = commentRepository.findById(commentId);
            if (optionalComment.isPresent()) {
                if (commentRepository.findById(commentId).get().getUser().getIp().equals(user.getIp())) {
                    post.deleteComment(commentRepository.findById(commentId).get());
                    commentRepository.deleteById(commentId);
                    postRepository.save(post);
                } else {
                    redirectAttributes.addFlashAttribute("message", "자신의 글만 지울 수 있습니다.");
                }
            }

        }
        return "redirect:/news1";
    }

    @GetMapping("/news3")
    public String news3(Model model) {
        Post post = postRepository.findByName("news3");
        List<Comment> comments = post.getComments();

        if (!userRepository.existsByIp(userService.getUserIp())) {
            User user = userService.saveNewUser();
            post.addLikedUser(user);
            post.addSharedUser(user);
            model.addAttribute(user);

        } else {
            User user = userRepository.findByIp(userService.getUserIp());
            post.addLikedUser(user);
            post.addSharedUser(user);
            model.addAttribute(user);
        }
        model.addAttribute(post);
        model.addAttribute(new CommentForm());
        model.addAttribute("commentList", comments);

        return "news2";
    }

    @PostMapping("/news3")
    public String postNews3(RedirectAttributes redirectAttributes,
                            @RequestParam(required = false) String like, @RequestParam(required = false) String share,
                            @Valid CommentForm commentForm, Errors errors) {
        if (errors.hasErrors()) {
            redirectAttributes.addFlashAttribute("contentError", "댓글 길이를 준수해주세요.");
            return "redirect:/home";
        }
        Post post = postRepository.findByName("news2");
        User user = userRepository.findByIp(userService.getUserIp());
        if (like != null) {
            if (!postRepository.existsByLikedUsers(user)) {
                post.addLikedUser(user);
                postRepository.save(post);
                redirectAttributes.addFlashAttribute("message", "좋아요 누름");
            } else {
                post.deleteLikedUser(user);
                postRepository.save(post);
                redirectAttributes.addFlashAttribute("message", "좋아요 취소");
            }
        }

        if (share != null) {
            if (!postRepository.existsBySharedUsers(user)) {
                post.addSharedUser(user);
                postRepository.save(post);
                redirectAttributes.addFlashAttribute("message", "공유 누름");
            } else {
                redirectAttributes.addFlashAttribute("message", "공유는 한번만 할 수 있습니다.");
            }
        }

        if (commentForm.getContent() != null) {
            Comment comment = commentService.saveNewComment(commentForm, post, user);
            post.addComment(comment);
            user.addComment(comment);
        }
        return "redirect:/news2";
    }


    @GetMapping("/news2")
    public String news2(Model model) {
        Post post = postRepository.findByName("news2");
        List<Comment> comments = post.getComments();

        if (!userRepository.existsByIp(userService.getUserIp())) {
            User user = userService.saveNewUser();
            post.addLikedUser(user);
            post.addSharedUser(user);
            model.addAttribute(user);

        } else {
            User user = userRepository.findByIp(userService.getUserIp());
            post.addLikedUser(user);
            post.addSharedUser(user);
            model.addAttribute(user);
        }
        model.addAttribute(post);
        model.addAttribute(new CommentForm());
        model.addAttribute("commentList", comments);

        return "news2";
    }

    @PostMapping("/news2")
    public String postNews2(RedirectAttributes redirectAttributes,
                            @RequestParam(required = false) String like, @RequestParam(required = false) String share,
                            @Valid CommentForm commentForm, Errors errors) {
        if (errors.hasErrors()) {
            redirectAttributes.addFlashAttribute("contentError", "댓글 길이를 준수해주세요.");
            return "redirect:/home";
        }
        Post post = postRepository.findByName("news2");
        User user = userRepository.findByIp(userService.getUserIp());
        if (like != null) {
            if (!postRepository.existsByLikedUsers(user)) {
                post.addLikedUser(user);
                postRepository.save(post);
                redirectAttributes.addFlashAttribute("message", "좋아요 누름");
            } else {
                post.deleteLikedUser(user);
                postRepository.save(post);
                redirectAttributes.addFlashAttribute("message", "좋아요 취소");
            }
        }

        if (share != null) {
            if (!postRepository.existsBySharedUsers(user)) {
                post.addSharedUser(user);
                postRepository.save(post);
                redirectAttributes.addFlashAttribute("message", "공유 누름");
            } else {
                redirectAttributes.addFlashAttribute("message", "공유는 한번만 할 수 있습니다.");
            }
        }

        if (commentForm.getContent() != null) {
            Comment comment = commentService.saveNewComment(commentForm, post, user);
            post.addComment(comment);
            user.addComment(comment);
        }
        return "redirect:/news2";
    }
}
