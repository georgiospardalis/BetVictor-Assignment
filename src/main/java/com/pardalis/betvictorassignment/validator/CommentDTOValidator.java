package com.pardalis.betvictorassignment.validator;

import com.pardalis.betvictorassignment.dto.CommentDTO;
import com.pardalis.betvictorassignment.helper.exception.CommentDTOValidationException;

public interface CommentDTOValidator {
    void validateDTO(CommentDTO commentDTO) throws CommentDTOValidationException;
}
