package com.newsletter.skuniv.domain;

import lombok.*;

import javax.persistence.*;

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

    private boolean like = false;

    private boolean share = false;

    @ManyToOne
    private Post post;
}
