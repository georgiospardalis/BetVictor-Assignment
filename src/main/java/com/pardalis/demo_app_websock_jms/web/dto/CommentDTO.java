package com.pardalis.demo_app_websock_jms.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class CommentDTO {
    @JsonProperty("email")
    @Email(message = "Must provide a valid email")
    private final String email;

    @JsonProperty("comment-text")
    @NotBlank(message = "Must provide comment")
    private final String commentText;

    public CommentDTO(String email, String commentText) {
        this.email = email;
        this.commentText = commentText;
    }

    public String getEmail() {
        return email;
    }

    public String getCommentText() {
        return commentText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CommentDTO that = (CommentDTO) o;

        return email.equals(that.email) &&
                commentText.equals(that.commentText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, commentText);
    }
}