package com.pardalis.betvictorassignment.web;

import com.pardalis.betvictorassignment.dto.CommentDTO;
import com.pardalis.betvictorassignment.dto.DisplayableCommentDTO;
import com.pardalis.betvictorassignment.helper.enumeration.CommentAction;
import com.pardalis.betvictorassignment.helper.exception.CommentDTOValidationException;
import com.pardalis.betvictorassignment.model.ReviewableComment;
import com.pardalis.betvictorassignment.receiver.MessageDestinations;
import com.pardalis.betvictorassignment.service.CommentService;
import com.pardalis.betvictorassignment.validator.CommentDTOValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CommentWebSocketImpl implements CommentWebSocket {
    private final static Logger LOGGER = LoggerFactory.getLogger(CommentWebSocketImpl.class);

    private CommentService commentService;

    private JmsTemplate jmsTemplate;

    private CommentDTOValidator commentDTOValidator;

    @Autowired
    public CommentWebSocketImpl(CommentService commentService,
                                JmsTemplate jmsTemplate,
                                CommentDTOValidator commentDTOValidator) {
        this.commentService = commentService;
        this.jmsTemplate = jmsTemplate;
        this.commentDTOValidator = commentDTOValidator;
    }

    @Override
    @SubscribeMapping("/thread/comments")
    public List<DisplayableCommentDTO> onSubscribe() {
        List<DisplayableCommentDTO> displayableCommentDTOs = new ArrayList<>();

        try {
            displayableCommentDTOs = commentService.findAllPersistedComments();
        } catch (Exception e) {
            LOGGER.warn("Could not send accepted comments to newly subscribed client", e);
        }

        return displayableCommentDTOs;
    }

    @Override
    @MessageMapping("/comment")
    public String onMessage(CommentDTO commentDTO) {
        String commentAction = CommentAction.COMMENT_NOT_SENT.toString();

        try {
            commentDTOValidator.validateDTO(commentDTO);
            jmsTemplate.convertAndSend(MessageDestinations.QUEUE_FOR_REVIEW, new ReviewableComment(
                    commentDTO.getEmail(),
                    commentDTO.getCommentText(),
                    System.currentTimeMillis()
            ));

            commentAction = CommentAction.COMMENT_FOR_REVIEW.toString();
        } catch (Exception e) {
            LOGGER.warn("Could not process new comment", e);

            if (e.getClass().equals(CommentDTOValidationException.class)) {
                commentAction = e.getMessage();
            }
        }

        return commentAction;
    }
}