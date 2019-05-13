package com.pardalis.betvictorassignment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DisplayableCommentDTO extends CommentDTO {
    @JsonProperty("timestamp")
    private final Long timestamp;

    public DisplayableCommentDTO(String email, String commentText, Long timestamp) {
        super(email, commentText);
        this.timestamp = timestamp;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}