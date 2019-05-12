package com.pardalis.betvictorassignment.service;

import com.pardalis.betvictorassignment.dto.DisplayableCommentDTO;
import com.pardalis.betvictorassignment.model.AcceptedComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private MongoOperations mongoOperations;

    @Autowired
    public CommentServiceImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
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
    public AcceptedComment saveAcceptedComment(AcceptedComment acceptedComment) {
        return mongoOperations.save(acceptedComment, "accepted_comments");
    }
}