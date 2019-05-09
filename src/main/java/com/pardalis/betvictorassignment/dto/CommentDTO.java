package com.pardalis.betvictorassignment.dto;

public class CommentDTO {
    private final String email;

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