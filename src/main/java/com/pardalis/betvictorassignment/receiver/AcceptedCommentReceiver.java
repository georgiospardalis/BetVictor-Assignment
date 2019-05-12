package com.pardalis.betvictorassignment.receiver;

import com.pardalis.betvictorassignment.dto.DisplayableCommentDTO;
import com.pardalis.betvictorassignment.model.AcceptedComment;
import com.pardalis.betvictorassignment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class AcceptedCommentReceiver {
    @Autowired
    private CommentService commentService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @JmsListener(destination = MessageDestinations.QUEUE_ACCEPTED, containerFactory = "jmsListenerContainerFactory")
    public void onConsume(AcceptedComment acceptedComment) throws Exception {
        commentService.saveAcceptedComment(acceptedComment);
        publishNewComment(acceptedComment);
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