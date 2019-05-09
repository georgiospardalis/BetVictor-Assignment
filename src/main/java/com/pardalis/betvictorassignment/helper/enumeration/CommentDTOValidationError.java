package com.pardalis.betvictorassignment.helper.enumeration;

public enum CommentDTOValidationError {
    INVALID_EMAIL("Invalid Email"),
    NULL_COMMENT("No comment provided");

    private final String value;

    CommentDTOValidationError(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}