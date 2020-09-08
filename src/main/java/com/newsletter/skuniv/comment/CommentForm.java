package com.newsletter.skuniv.comment;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CommentForm {

    @Length(min = 5)
    private String content;
}
