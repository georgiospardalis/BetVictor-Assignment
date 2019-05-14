package com.pardalis.betvictorassignment.service;

import com.pardalis.betvictorassignment.dto.CommentDTO;
import com.pardalis.betvictorassignment.dto.DisplayableCommentDTO;
import com.pardalis.betvictorassignment.model.AcceptedComment;

import java.util.List;

public interface CommentService {
    List<DisplayableCommentDTO> findAllPersistedComments();

    String sendCommentForReview(CommentDTO commentDTO);

    AcceptedComment saveAcceptedComment(AcceptedComment acceptedComment);
}