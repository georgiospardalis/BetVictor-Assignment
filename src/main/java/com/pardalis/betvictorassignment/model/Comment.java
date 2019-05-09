package com.pardalis.betvictorassignment.model;

public final class Comment {
    private final String email;
    private final String commentText;
    private final Long timestamp;

    public Comment(String email, String commentText, Long timestamp) {
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