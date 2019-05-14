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
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

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
    @SubscribeMapping("/comments")
    public List<DisplayableCommentDTO> onSubscribe() {
        return commentService.findAllPersistedComments();
    }

    @Override
    @MessageMapping("/comment")
    @SendToUser("/thread/comment_action")
    public String onMessage(CommentDTO commentDTO) throws Exception {
        commentDTOValidator.validateDTO(commentDTO);

        return commentService.sendCommentForReview(commentDTO);
    }

    @MessageExceptionHandler
    @SendToUser("/thread/error")
    public String onException(Exception e) {
        LOGGER.error("Error Handler Triggered", e);

        if (e.getClass().equals(CommentDTOValidationException.class)) {
            return e.getMessage();
        } else {
            return "Unknown internal error";
        }
    }
}