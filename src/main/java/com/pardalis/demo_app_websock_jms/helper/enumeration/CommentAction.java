package com.pardalis.demo_app_websock_jms.helper.enumeration;

public enum CommentAction {
    COMMENT_FOR_REVIEW("Comment is sent for review");

    private final String value;

    CommentAction(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}