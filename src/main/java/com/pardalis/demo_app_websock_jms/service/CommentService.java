package com.pardalis.demo_app_websock_jms.service;

import com.pardalis.demo_app_websock_jms.web.dto.CommentDTO;
import com.pardalis.demo_app_websock_jms.web.dto.DisplayableCommentDTO;
import com.pardalis.demo_app_websock_jms.messaging.model.AcceptedComment;

import java.util.List;

public interface CommentService {
    List<DisplayableCommentDTO> findAllPersistedComments();

    String sendCommentForReview(CommentDTO commentDTO);

    AcceptedComment saveAcceptedComment(AcceptedComment acceptedComment);
}