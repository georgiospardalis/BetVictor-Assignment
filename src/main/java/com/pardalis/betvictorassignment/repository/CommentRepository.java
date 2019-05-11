package com.pardalis.betvictorassignment.repository;

import com.pardalis.betvictorassignment.model.AcceptedComment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends MongoRepository<AcceptedComment, String> {
}