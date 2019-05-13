package com.pardalis.betvictorassignment.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

public final class AcceptedComment extends ReviewableComment {
    @Id
    private String id;

    @JsonProperty("timestamp-accepted")
    private final Long timestampAccepted;

    @JsonCreator
    public AcceptedComment(@JsonProperty("email") String email,
                           @JsonProperty("comment-text") String commentText,
                           @JsonProperty("timestamp") Long timestamp,
                           @JsonProperty("timestamp-accepted") Long timestampAccepted) {
        super(email, commentText, timestamp);
        this.timestampAccepted = timestampAccepted;
    }

    public AcceptedComment(String id, String email, String commentText, Long timestamp, Long timestampAccepted) {
        super(email, commentText, timestamp);
        this.id = id;
        this.timestampAccepted = timestampAccepted;
    }

    public Long getTimestampAccepted() {
        return timestampAccepted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}