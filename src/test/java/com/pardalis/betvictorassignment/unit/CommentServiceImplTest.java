package com.pardalis.betvictorassignment.unit;

import com.pardalis.betvictorassignment.dto.CommentDTO;
import com.pardalis.betvictorassignment.dto.DisplayableCommentDTO;
import com.pardalis.betvictorassignment.helper.enumeration.CommentAction;
import com.pardalis.betvictorassignment.model.AcceptedComment;
import com.pardalis.betvictorassignment.model.ReviewableComment;
import com.pardalis.betvictorassignment.service.CommentServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.DestinationResolutionException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CommentServiceImplTest {
    @Mock
    private MongoOperations mongoOperations;

    @Mock
    private JmsTemplate jmsTemplate;

    @InjectMocks
    private CommentServiceImpl commentServiceImpl;

    @Test
    public void findAllPersistedComments_returnEmpty() {
        when(mongoOperations.findAll(AcceptedComment.class, "accepted_comments"))
                .thenReturn(new ArrayList<AcceptedComment>());

        List<DisplayableCommentDTO> returnedDisplayableComments = commentServiceImpl.findAllPersistedComments();

        Assert.assertEquals(0, returnedDisplayableComments.size());
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

        Assert.assertEquals(1, returnedDisplayableComments.size());
        Assert.assertEquals(expectedDTO.getTimestamp(), returnedDisplayableComments.get(0).getTimestamp());
        Assert.assertEquals(expectedDTO.getCommentText(), returnedDisplayableComments.get(0).getCommentText());
        Assert.assertEquals(expectedDTO.getEmail(), returnedDisplayableComments.get(0).getEmail());
    }

    @Test
    public void sendCommentForReview_success() {
        CommentDTO commentDTO = new CommentDTO("mail@mail.com", "ignore me");

        doNothing().when(jmsTemplate).convertAndSend(any(String.class), any(CommentDTO.class));

        String msg = commentServiceImpl.sendCommentForReview(commentDTO);

        Assert.assertEquals(CommentAction.COMMENT_FOR_REVIEW.toString(), msg);
        verify(jmsTemplate, times(1)).convertAndSend(any(String.class), any(ReviewableComment.class));
    }

    @Test
    public void sendCommentForReview_exception() {
        CommentDTO commentDTO = new CommentDTO("mail@mail.com", "ignore me");

        doThrow(new DestinationResolutionException("")).when(jmsTemplate).convertAndSend(any(String.class), any(ReviewableComment.class));

        try {
            commentServiceImpl.sendCommentForReview(commentDTO);
            Assert.fail();
        } catch (JmsException e) {

        }

        verify(jmsTemplate, times(1)).convertAndSend(any(String.class), any(ReviewableComment.class));
    }

    @Test
    public void saveAcceptedComment_success() {
        AcceptedComment acceptedComment = new AcceptedComment(
                new Long("1234567890123"),
                "email@mail.com",
                "ignore me",
                new Long("1234567890123"));

        when(mongoOperations.save(any(AcceptedComment.class), eq("accepted_comments"))).thenReturn(acceptedComment);

        commentServiceImpl.saveAcceptedComment(acceptedComment);

        verify(mongoOperations, times(1)).save(any(AcceptedComment.class), any(String.class));
    }

    @Test
    public void saveAcceptedComment_exception() {
        AcceptedComment acceptedComment = new AcceptedComment(
                new Long("1234567890123"),
                "email@mail.com",
                "ignore me",
                new Long("1234567890123"));

        doThrow(new RuntimeException()).when(mongoOperations).save(any(AcceptedComment.class), eq("accepted_comments"));

        try {
            commentServiceImpl.saveAcceptedComment(acceptedComment);
            Assert.fail();
        } catch (Exception e) {

        }

        verify(mongoOperations, times(1)).save(any(AcceptedComment.class), any(String.class));
    }
}