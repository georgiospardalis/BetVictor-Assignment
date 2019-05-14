package com.pardalis.betvictorassignment.unit;

import com.pardalis.betvictorassignment.dto.DisplayableCommentDTO;
import com.pardalis.betvictorassignment.model.AcceptedComment;
import com.pardalis.betvictorassignment.service.CommentServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoOperations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CommentServiceImplTest {
    @Mock
    private MongoOperations mongoOperations;

    @InjectMocks
    private CommentServiceImpl commentServiceImpl;

    @Test
    public void findAllPersistedComments_returnEmpty() {
        when(mongoOperations.findAll(AcceptedComment.class, "accepted_comments"))
                .thenReturn(new ArrayList<AcceptedComment>());

        List<DisplayableCommentDTO> returnedDisplayableComments = commentServiceImpl.findAllPersistedComments();

        Assert.assertEquals(returnedDisplayableComments.size(), 0);
    }

    @Test
    public void findAllPersistedComments_returnComments() {
        AcceptedComment acceptedComment = new AcceptedComment(
                "someId",
                new Long("1234567890123"),
                "email@mail.com",
                "ignore me", new Long("1234567890123"));

        List<AcceptedComment> acceptedCommentList = new ArrayList<>();
        acceptedCommentList.add(acceptedComment);

        DisplayableCommentDTO expectedDTO = new DisplayableCommentDTO(
                "email@mail.com",
                "ignore me",
                new Long("1234567890123"));

        when(mongoOperations.findAll(AcceptedComment.class, "accepted_comments"))
                .thenReturn(acceptedCommentList);

        List<DisplayableCommentDTO> returnedDisplayableComments = commentServiceImpl.findAllPersistedComments();

        Assert.assertEquals(returnedDisplayableComments.size(), 1);
        Assert.assertEquals(returnedDisplayableComments.get(0).getTimestamp(), expectedDTO.getTimestamp());
        Assert.assertEquals(returnedDisplayableComments.get(0).getCommentText(), expectedDTO.getCommentText());
        Assert.assertEquals(returnedDisplayableComments.get(0).getEmail(), expectedDTO.getEmail());
    }
}