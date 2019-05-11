package com.pardalis.betvictorassignment.service;

import com.pardalis.betvictorassignment.dto.DisplayableCommentDTO;
import com.pardalis.betvictorassignment.model.AcceptedComment;

import java.util.List;

public interface CommentService {
    List<DisplayableCommentDTO> findAllPersistedComments();

    AcceptedComment saveAcceptedComment(AcceptedComment acceptedComment);
}
