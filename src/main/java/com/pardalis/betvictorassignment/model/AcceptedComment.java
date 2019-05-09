package com.pardalis.betvictorassignment.model;

public final class AcceptedComment extends ReviewableComment {
    private final Long timestampAccepted;

    public AcceptedComment(String email, String commentText, Long timestamp, Long timestampAccepted) {
        super(email, commentText, timestamp);
        this.timestampAccepted = timestampAccepted;
    }

    public Long getTimestampAccepted() {
        return timestampAccepted;
    }
}