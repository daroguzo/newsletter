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
public class User {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String ip;

    @ManyToMany
    private List<Post> likedPost = new ArrayList<>();

    @ManyToMany
    private List<Post> sharedPost = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    public void addComment(Comment comment) {
        this.getComments().add(comment);
        comment.setUser(this);
    }

    public void deleteComment(Comment comment) {
        this.getComments().remove(comment);
        comment.setUser(null);
    }
}
