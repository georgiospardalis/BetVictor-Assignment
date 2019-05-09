package com.pardalis.betvictorassignment.model;

public final class AcceptedComment extends ReviewableComment {
    private final Long timestampAccepted;

    public AcceptedComment(String email, String commentText, Long timestampAccepted) {
        super(email, commentText);
        this.timestampAccepted = timestampAccepted;
    }

    public Long getTimestampAccepted() {
        return timestampAccepted;
    }
}