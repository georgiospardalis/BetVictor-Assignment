package com.pardalis.betvictorassignment.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ReviewableComment {
    @JsonProperty("email")
    private final String email;

    @JsonProperty("comment-text")
    private final String commentText;

    @JsonProperty("timestamp")
    private final Long timestamp;

    @JsonCreator
    public ReviewableComment(@JsonProperty("email") String email,
                             @JsonProperty("comment-text") String commentText,
                             @JsonProperty("timestamp") Long timestamp) {
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