package com.pardalis.comments_thread.web.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        if (!super.equals(o)) {
            return false;
        }

        DisplayableCommentDTO that = (DisplayableCommentDTO) o;

        return timestamp.equals(that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), timestamp);
    }
}