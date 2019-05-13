package com.pardalis.betvictorassignment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommentDTO {
    @JsonProperty("email")
    private final String email;

    @JsonProperty("comment-text")
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