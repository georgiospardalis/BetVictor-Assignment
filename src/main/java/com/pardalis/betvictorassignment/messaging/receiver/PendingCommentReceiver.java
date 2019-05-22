package com.pardalis.betvictorassignment.messaging.receiver;

import com.pardalis.betvictorassignment.messaging.MessageDestinations;
import com.pardalis.betvictorassignment.messaging.model.AcceptedComment;
import com.pardalis.betvictorassignment.messaging.model.ReviewableComment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class PendingCommentReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(PendingCommentReceiver.class);

    private JmsTemplate jmsTemplate;

    @Autowired
    public PendingCommentReceiver(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @JmsListener(destination = MessageDestinations.QUEUE_FOR_REVIEW, containerFactory = "jmsListenerContainerFactory")
    public void onConsume(ReviewableComment reviewableComment) throws Exception {
        LOGGER.info("Consumed from queue "
                + MessageDestinations.QUEUE_FOR_REVIEW
                + ", message:"
                + reviewableComment.toString());

        AcceptedComment acceptedComment = reviewComment(reviewableComment);
        forwardAcceptedComment(acceptedComment);

        LOGGER.info("Produced on queue "
                + MessageDestinations.QUEUE_ACCEPTED
                + ", message:"
                + acceptedComment.toString());
    }

    private AcceptedComment reviewComment(ReviewableComment reviewableComment) {
        return new AcceptedComment(
                System.currentTimeMillis(),
                reviewableComment.getEmail(),
                reviewableComment.getCommentText(),
                reviewableComment.getTimestamp());
    }

    private void forwardAcceptedComment(AcceptedComment acceptedComment) {
        jmsTemplate.convertAndSend(MessageDestinations.QUEUE_ACCEPTED, acceptedComment);
    }
}