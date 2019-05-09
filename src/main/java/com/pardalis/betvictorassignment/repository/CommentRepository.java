package com.pardalis.betvictorassignment.repository;

import com.pardalis.betvictorassignment.model.AcceptedComment;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface CommentRepository extends MongoRepository<AcceptedComment, String> {
}