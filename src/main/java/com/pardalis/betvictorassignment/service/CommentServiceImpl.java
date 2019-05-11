package com.pardalis.betvictorassignment.service;

import com.pardalis.betvictorassignment.dto.DisplayableCommentDTO;
import com.pardalis.betvictorassignment.model.AcceptedComment;
import com.pardalis.betvictorassignment.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

//TODO FIX ME

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<DisplayableCommentDTO> findAllPersistedComments() {
        List<AcceptedComment> acceptedCommentList = commentRepository.findAll();

        return acceptedCommentList
                .stream()
                .map(ac -> new DisplayableCommentDTO(ac.getEmail(), ac.getCommentText(), ac.getTimestamp()))
                .collect(Collectors.toList());
    }

    @Override
    public AcceptedComment saveAcceptedComment(AcceptedComment acceptedComment) {
        return commentRepository.save(acceptedComment);
    }
}