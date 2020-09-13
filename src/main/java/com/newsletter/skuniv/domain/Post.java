package com.newsletter.skuniv.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
public class Post {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    private Integer likeCount = 0;

    private Integer shareCount = 0;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany
    private List<User> likedUsers = new ArrayList<>();

    @ManyToMany
    private List<User> sharedUsers = new ArrayList<>();

    public void addComment(Comment comment) {
        this.getComments().add(comment);
        comment.setPost(this);
    }

    public void deleteComment(Comment comment) {
        this.getComments().remove(comment);
        comment.setPost(null);

    }

    public void addLikedUser(User user) {
        this.getLikedUsers().add(user);
//        user.setLikedPost(this);
    }

    public void addSharedUser(User user) {
        this.getLikedUsers().add(user);
//        user.setSharedPost(this);
    }

    public void deleteLikedUser(User user) {
        this.getLikedUsers().remove(user);
//        user.setLikedPost(null);
    }

    public void deleteSharedUser(User user) {
        this.getSharedUsers().remove(user);
//        user.setSharedPost(null);
    }
}
