package com.pardalis.betvictorassignment.model;

import org.springframework.data.annotation.Id;

public final class AcceptedComment extends ReviewableComment {
    @Id
    private String id;

    private final Long timestampAccepted;

    public AcceptedComment(String email, String commentText, Long timestamp, Long timestampAccepted) {
        super(email, commentText, timestamp);
        this.timestampAccepted = timestampAccepted;
    }

    public AcceptedComment(String email, String commentText, Long timestamp, String id, Long timestampAccepted) {
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