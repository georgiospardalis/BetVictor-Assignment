package com.pardalis.betvictorassignment.helper.enumeration;

public enum CommentAction {
    COMMENT_FOR_REVIEW("Comment is sent for review"),
    COMMENT_NOT_SENT("Could not sent comment for review");

    private final String value;

    CommentAction(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}