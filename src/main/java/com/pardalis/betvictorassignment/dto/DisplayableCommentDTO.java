package com.pardalis.betvictorassignment.dto;

public class DisplayableCommentDTO extends CommentDTO {
    private final Long timestamp;

    public DisplayableCommentDTO(String email, String commentText, Long timestamp) {
        super(email, commentText);
        this.timestamp = timestamp;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}