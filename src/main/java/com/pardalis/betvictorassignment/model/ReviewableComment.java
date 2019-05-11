package com.pardalis.betvictorassignment.model;

public class ReviewableComment {
    private final String email;

    private final String commentText;

    private final Long timestamp;

    public ReviewableComment(String email, String commentText, Long timestamp) {
        this.email = email;
        this.commentText = commentText;
        this.timestamp = timestamp;
    }

    public String getEmail() {
        return email;
    }

    public String getCommentText() {
        return commentText;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}