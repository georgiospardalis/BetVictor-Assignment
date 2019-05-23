package com.pardalis.betvictorassignment.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

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


}