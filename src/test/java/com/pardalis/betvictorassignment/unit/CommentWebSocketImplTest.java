package com.pardalis.betvictorassignment.unit;

import com.pardalis.betvictorassignment.web.dto.CommentDTO;
import com.pardalis.betvictorassignment.web.dto.DisplayableCommentDTO;
import com.pardalis.betvictorassignment.helper.enumeration.CommentAction;
import com.pardalis.betvictorassignment.service.CommentServiceImpl;
import com.pardalis.betvictorassignment.web.socket.CommentWebSocketImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jms.support.destination.DestinationResolutionException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CommentWebSocketImplTest {
    @Mock
    private CommentServiceImpl commentServiceImpl;

    @InjectMocks
    private CommentWebSocketImpl commentWebSocketImpl;

    @Test
    public void getPreexistingCommentsOnSubscribe() {
        DisplayableCommentDTO displayableCommentDTO = new DisplayableCommentDTO(
                "email@mail.com",
                "ignore me",
                new Long("1234567890123"));

        List<DisplayableCommentDTO> returnedList = new ArrayList<>();
        returnedList.add(displayableCommentDTO);

        when(commentServiceImpl.findAllPersistedComments()).thenReturn(returnedList);

        List<DisplayableCommentDTO> finalList = commentServiceImpl.findAllPersistedComments();

        Assert.assertEquals(1, finalList.size());
        Assert.assertEquals("email@mail.com", finalList.get(0).getEmail());
        Assert.assertEquals("ignore me", finalList.get(0).getCommentText());
        Assert.assertEquals(new Long("1234567890123"), finalList.get(0).getTimestamp());
    }

    @Test
    public void getNothingOnSubscribe() {
        when(commentServiceImpl.findAllPersistedComments()).thenReturn(new ArrayList<>());

        List<DisplayableCommentDTO> returnedList = commentWebSocketImpl.onSubscribe();

        Assert.assertEquals(0, returnedList.size());
    }

    @Test
    public void sendCommentSuccessfull() {
        when(commentServiceImpl.sendCommentForReview(any(CommentDTO.class))).thenReturn(CommentAction.COMMENT_FOR_REVIEW.toString());

        String msg = null;
        try {
            msg = commentWebSocketImpl.onMessage(new CommentDTO("email@mail.com", "ignored"));

        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNotNull(msg);
        Assert.assertEquals(CommentAction.COMMENT_FOR_REVIEW.toString(), msg);
        verify(commentServiceImpl, times(1)).sendCommentForReview(any(CommentDTO.class));
    }

    @Test
    public void sendCommentFailedJms() {
        doThrow(new DestinationResolutionException("")).when(commentServiceImpl).sendCommentForReview(any(CommentDTO.class));

        try {
            commentWebSocketImpl.onMessage(new CommentDTO("email@mail.com", "ignored"));
            Assert.fail();
        } catch (Exception e) {

        }

        verify(commentServiceImpl, times(1)).sendCommentForReview(any(CommentDTO.class));
    }

    @Test
    public void sendCommentFailedValidation() {
        try {
            commentWebSocketImpl.onMessage(new CommentDTO("", ""));
            Assert.fail();
        } catch (Exception e) {

        }
        verify(commentServiceImpl, times(0)).sendCommentForReview(any(CommentDTO.class));
    }
}