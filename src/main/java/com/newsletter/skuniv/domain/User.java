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

   private boolean likePost;

    private boolean share;

    @ManyToOne
    private Post post;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    public void addPost(Comment comment) {
        this.getComments().add(comment);
        comment.setUser(this);
    }
}
