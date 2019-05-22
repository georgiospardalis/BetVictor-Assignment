package com.pardalis.betvictorassignment.messaging.receiver;

import com.pardalis.betvictorassignment.web.dto.DisplayableCommentDTO;
import com.pardalis.betvictorassignment.messaging.MessageDestinations;
import com.pardalis.betvictorassignment.messaging.model.AcceptedComment;
import com.pardalis.betvictorassignment.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class AcceptedCommentReceiver {
    private final static Logger LOGGER = LoggerFactory.getLogger(AcceptedCommentReceiver.class);

    private CommentService commentService;

    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public AcceptedCommentReceiver(CommentService commentService, SimpMessagingTemplate simpMessagingTemplate) {
        this.commentService = commentService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @JmsListener(destination = MessageDestinations.QUEUE_ACCEPTED, containerFactory = "jmsListenerContainerFactory")
    public void onConsume(AcceptedComment acceptedComment) throws Exception {
        LOGGER.info("Consumed new message on queue "
                + MessageDestinations.QUEUE_ACCEPTED
                + ", message:"
                + acceptedComment.toString());

        commentService.saveAcceptedComment(acceptedComment);
        publishNewComment(acceptedComment);

        LOGGER.info("Published on websocket broker '/thread/comments', message:" + acceptedComment.toString());
    }

    private void publishNewComment(AcceptedComment acceptedComment) {
        DisplayableCommentDTO displayableCommentDTO = getDTOForAcceptedComment(acceptedComment);

        simpMessagingTemplate.convertAndSend("/thread/comments", displayableCommentDTO);
    }

    private DisplayableCommentDTO getDTOForAcceptedComment(AcceptedComment acceptedComment) {
        return new DisplayableCommentDTO(
                acceptedComment.getEmail(),
                acceptedComment.getCommentText(),
                acceptedComment.getTimestampAccepted());
    }
}