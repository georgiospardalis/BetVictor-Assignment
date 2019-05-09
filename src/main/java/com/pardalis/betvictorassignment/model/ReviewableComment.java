package com.pardalis.betvictorassignment.model;

public class ReviewableComment {
    private final String email;
    private final String commentText;
    private final Long timestamp;

    public ReviewableComment(String email, String commentText) {
        this.email = email;
        this.commentText = commentText;
        this.timestamp = System.currentTimeMillis();
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