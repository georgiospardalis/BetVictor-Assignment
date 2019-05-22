package com.pardalis.betvictorassignment.service;

import com.pardalis.betvictorassignment.web.dto.CommentDTO;
import com.pardalis.betvictorassignment.web.dto.DisplayableCommentDTO;
import com.pardalis.betvictorassignment.helper.enumeration.CommentAction;
import com.pardalis.betvictorassignment.messaging.model.AcceptedComment;
import com.pardalis.betvictorassignment.messaging.model.ReviewableComment;
import com.pardalis.betvictorassignment.messaging.MessageDestinations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private MongoOperations mongoOperations;

    private JmsTemplate jmsTemplate;

    @Autowired
    public CommentServiceImpl(MongoOperations mongoOperations, JmsTemplate jmsTemplate) {
        this.mongoOperations = mongoOperations;
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public List<DisplayableCommentDTO> findAllPersistedComments() {
        return mongoOperations
                .findAll(AcceptedComment.class, "accepted_comments")
                .stream()
                .map(ac -> new DisplayableCommentDTO(
                        ac.getEmail(),
                        ac.getCommentText(),
                        ac.getTimestamp()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public String sendCommentForReview(CommentDTO commentDTO) {
        jmsTemplate.convertAndSend(MessageDestinations.QUEUE_FOR_REVIEW, new ReviewableComment(
                commentDTO.getEmail(),
                commentDTO.getCommentText(),
                System.currentTimeMillis()
        ));

        return CommentAction.COMMENT_FOR_REVIEW.toString();
    }

    @Override
    public AcceptedComment saveAcceptedComment(AcceptedComment acceptedComment) {
        return mongoOperations.save(acceptedComment, "accepted_comments");
    }
}