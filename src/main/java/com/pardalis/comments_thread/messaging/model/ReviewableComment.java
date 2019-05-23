package com.pardalis.comments_thread.messaging.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReviewableComment {
    @JsonProperty("email")
    private String email;

    @JsonProperty("comment-text")
    private String commentText;

    @JsonProperty("timestamp")
    private Long timestamp;

    public ReviewableComment() {

    }

    public ReviewableComment(String email, String commentText, Long timestamp) {
        this.email = email;
        this.commentText = commentText;
        this.timestamp = timestamp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ReviewableComment: {\n"
                + "email: " + email + "\n"
                + "comment:" + commentText + "\n"
                + "timestamp:" + timestamp + "\n"
                + "}";
    }
}