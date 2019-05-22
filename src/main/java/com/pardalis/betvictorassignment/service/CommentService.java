package com.pardalis.betvictorassignment.service;

import com.pardalis.betvictorassignment.web.dto.CommentDTO;
import com.pardalis.betvictorassignment.web.dto.DisplayableCommentDTO;
import com.pardalis.betvictorassignment.messaging.model.AcceptedComment;

import java.util.List;

public interface CommentService {
    List<DisplayableCommentDTO> findAllPersistedComments();

    String sendCommentForReview(CommentDTO commentDTO);

    AcceptedComment saveAcceptedComment(AcceptedComment acceptedComment);
}