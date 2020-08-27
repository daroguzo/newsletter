package com.newsletter.skuniv.domain;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
public class Post {

    @Id @GeneratedValue
    private Long id;

    private Integer likeCount = 0;

    private Integer shareCount = 0;

    @OneToMany(mappedBy = "post")
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "post")
    private Set<User> users = new HashSet<>();

    public void addComment(Comment comment) {
        this.getComments().add(comment);
        comment.setPost(this);
    }

    public void addUser(User user) {
        this.getUsers().add(user);
        user.setPost(this);
    }
}
