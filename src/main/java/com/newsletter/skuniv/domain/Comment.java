package com.newsletter.skuniv.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor
@Builder @AllArgsConstructor
@Entity
public class Comment {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String password;

    @Lob
    private String content;

    private LocalDateTime created;

    @ManyToOne
    private Post post;


}
