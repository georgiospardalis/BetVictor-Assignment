package com.pardalis.demo_app_websock_jms.unit;

import com.pardalis.demo_app_websock_jms.web.dto.CommentDTO;
import com.pardalis.demo_app_websock_jms.web.dto.DisplayableCommentDTO;
import com.pardalis.demo_app_websock_jms.helper.enumeration.CommentAction;
import com.pardalis.demo_app_websock_jms.service.CommentServiceImpl;
import com.pardalis.demo_app_websock_jms.web.socket.CommentWebSocketImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jms.JmsException;
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

        DisplayableCommentDTO expectedCommentDTO = new DisplayableCommentDTO(
                "email@mail.com",
                "ignore me",
                new Long("1234567890123"));

        List<DisplayableCommentDTO> expectedList = new ArrayList<>();
        expectedList.add(expectedCommentDTO);

        when(commentServiceImpl.findAllPersistedComments()).thenReturn(returnedList);
        List<DisplayableCommentDTO> finalList = commentServiceImpl.findAllPersistedComments();

        Assert.assertEquals(expectedList, finalList);
    }

    @Test
    public void getNothingOnSubscribe() {
        when(commentServiceImpl.findAllPersistedComments()).thenReturn(new ArrayList<>());

        List<DisplayableCommentDTO> returnedList = commentWebSocketImpl.onSubscribe();

        Assert.assertEquals(0, returnedList.size());
    }

    @Test
    public void sendCommentSuccessfull() throws Exception {
        when(commentServiceImpl.sendCommentForReview(any(CommentDTO.class))).thenReturn(CommentAction.COMMENT_FOR_REVIEW.toString());
        String msg = commentWebSocketImpl.onMessage(new CommentDTO("email@mail.com", "ignored"));

        Assert.assertNotNull(msg);
        Assert.assertEquals(CommentAction.COMMENT_FOR_REVIEW.toString(), msg);
        verify(commentServiceImpl, times(1)).sendCommentForReview(any(CommentDTO.class));
    }

    @Test(expected = JmsException.class)
    public void sendCommentFailedJms() {
        doThrow(new DestinationResolutionException("")).when(commentServiceImpl).sendCommentForReview(any(CommentDTO.class));
        commentWebSocketImpl.onMessage(new CommentDTO("email@mail.com", "ignored"));
    }
}