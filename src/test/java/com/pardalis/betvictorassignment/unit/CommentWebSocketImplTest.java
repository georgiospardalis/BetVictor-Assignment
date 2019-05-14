package com.pardalis.betvictorassignment.unit;

import com.pardalis.betvictorassignment.dto.CommentDTO;
import com.pardalis.betvictorassignment.dto.DisplayableCommentDTO;
import com.pardalis.betvictorassignment.helper.enumeration.CommentAction;
import com.pardalis.betvictorassignment.helper.exception.CommentDTOValidationException;
import com.pardalis.betvictorassignment.service.CommentServiceImpl;
import com.pardalis.betvictorassignment.validator.CommentDTOValidatorImpl;
import com.pardalis.betvictorassignment.web.CommentWebSocketImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jms.support.destination.DestinationResolutionException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CommentWebSocketImplTest {
    @Mock
    private CommentServiceImpl commentServiceImpl;

    @Mock
    private CommentDTOValidatorImpl commentDTOValidatorImpl;

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

        Assert.assertEquals(finalList.size(), 1);
        Assert.assertEquals(finalList.get(0).getEmail(), "email@mail.com");
        Assert.assertEquals(finalList.get(0).getCommentText(), "ignore me");
        Assert.assertEquals(finalList.get(0).getTimestamp(), new Long("1234567890123"));
    }

    @Test
    public void getNothingOnSubscribe() {
        when(commentServiceImpl.findAllPersistedComments()).thenReturn(new ArrayList<>());

        List<DisplayableCommentDTO> returnedList = commentWebSocketImpl.onSubscribe();

        Assert.assertEquals(returnedList.size(), 0);
    }

    @Test
    public void sendCommentSuccessfull() {
        doNothing().when(commentDTOValidatorImpl).validateDTO(any(CommentDTO.class));
        when(commentServiceImpl.sendCommentForReview(any(CommentDTO.class))).thenReturn(CommentAction.COMMENT_FOR_REVIEW.toString());

        String msg = null;
        try {
            msg = commentWebSocketImpl.onMessage(new CommentDTO("email@mail.com", "ignored"));

        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNotNull(msg);
        Assert.assertEquals(msg, CommentAction.COMMENT_FOR_REVIEW.toString());
        verify(commentDTOValidatorImpl, times(1)).validateDTO(any(CommentDTO.class));
        verify(commentServiceImpl, times(1)).sendCommentForReview(any(CommentDTO.class));
    }

    @Test
    public void sendCommentFailedJms() {
        doNothing().when(commentDTOValidatorImpl).validateDTO(any(CommentDTO.class));
        doThrow(new DestinationResolutionException("")).when(commentServiceImpl).sendCommentForReview(any(CommentDTO.class));

        try {
            commentWebSocketImpl.onMessage(new CommentDTO("email@mail.com", "ignored"));
            Assert.fail();
        } catch (Exception e) {

        }

        verify(commentDTOValidatorImpl, times(1)).validateDTO(any(CommentDTO.class));
        verify(commentServiceImpl, times(1)).sendCommentForReview(any(CommentDTO.class));
    }

    @Test
    public void sendCommentFailedValidation() {
        doThrow(new CommentDTOValidationException("")).when(commentDTOValidatorImpl).validateDTO(any(CommentDTO.class));

        try {
            commentWebSocketImpl.onMessage(new CommentDTO("", ""));
            Assert.fail();
        } catch (Exception e) {

        }

        verify(commentDTOValidatorImpl, times(1)).validateDTO(any(CommentDTO.class));
        verify(commentServiceImpl, times(0)).sendCommentForReview(any(CommentDTO.class));
    }
}