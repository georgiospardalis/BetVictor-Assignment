package com.pardalis.betvictorassignment.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class CommentDTO {
    @JsonProperty("email")
    @NotBlank(message = "Must provide a non-empty email")
    @Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", message = "Must provide a valid email")
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


}