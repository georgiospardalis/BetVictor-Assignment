package com.pardalis.betvictorassignment.receiver;

import com.pardalis.betvictorassignment.model.AcceptedComment;
import com.pardalis.betvictorassignment.model.ReviewableComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class PendingCommentReceiver {
    @Autowired
    private JmsTemplate jmsTemplate;

    @JmsListener(destination = MessageDestinations.QUEUE_FOR_REVIEW, containerFactory = "jmsListenerContainerFactory")
    public void onConsume(ReviewableComment reviewableComment) throws Exception {
        AcceptedComment acceptedComment = reviewComment(reviewableComment);

        forwardAcceptedComment(acceptedComment);
    }

    private AcceptedComment reviewComment(ReviewableComment reviewableComment) {
        return new AcceptedComment(
                reviewableComment.getEmail(),
                reviewableComment.getCommentText(),
                reviewableComment.getTimestamp(),
                System.currentTimeMillis());
    }

    private void forwardAcceptedComment(AcceptedComment acceptedComment) {
        jmsTemplate.convertAndSend(MessageDestinations.QUEUE_ACCEPTED, acceptedComment);
    }
}