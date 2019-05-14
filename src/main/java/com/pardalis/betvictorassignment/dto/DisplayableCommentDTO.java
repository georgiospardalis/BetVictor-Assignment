package com.pardalis.betvictorassignment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DisplayableCommentDTO extends CommentDTO {
    @JsonProperty("timestamp")
    private final Long timestamp;

    @JsonCreator
    public DisplayableCommentDTO(@JsonProperty("email") String email,
                                 @JsonProperty("comment-text") String commentText,
                                 @JsonProperty("timestamp") Long timestamp) {
        super(email, commentText);
        this.timestamp = timestamp;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}