package com.pardalis.comments_thread.service;

import com.pardalis.comments_thread.web.dto.CommentDTO;
import com.pardalis.comments_thread.web.dto.DisplayableCommentDTO;
import com.pardalis.comments_thread.messaging.model.AcceptedComment;

import java.util.List;

public interface CommentService {
    List<DisplayableCommentDTO> findAllPersistedComments();

    String sendCommentForReview(CommentDTO commentDTO);

    AcceptedComment saveAcceptedComment(AcceptedComment acceptedComment);
}