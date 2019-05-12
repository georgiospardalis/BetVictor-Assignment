package com.pardalis.betvictorassignment.validator;

import com.pardalis.betvictorassignment.dto.CommentDTO;
import com.pardalis.betvictorassignment.helper.enumeration.CommentDTOValidationError;
import com.pardalis.betvictorassignment.helper.exception.CommentDTOValidationException;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CommentDTOValidatorImpl implements CommentDTOValidator {
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    public void validateDTO(CommentDTO commentDTO) throws CommentDTOValidationException {
        validateEmail(commentDTO.getEmail());
        validateCommentText(commentDTO.getCommentText());
    }

    private void validateEmail(String email) throws CommentDTOValidationException {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);

        if (!matcher.find()) {
            throw new CommentDTOValidationException(CommentDTOValidationError.INVALID_EMAIL.toString());
        }
    }

    private void validateCommentText(String commentText) throws CommentDTOValidationException{
        if (commentText == null || commentText.equals("")) {
            throw new CommentDTOValidationException(CommentDTOValidationError.NULL_COMMENT.toString());
        }
    }
}