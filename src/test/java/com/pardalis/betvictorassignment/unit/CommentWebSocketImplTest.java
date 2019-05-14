package com.pardalis.betvictorassignment.unit;

import com.pardalis.betvictorassignment.dto.CommentDTO;
import com.pardalis.betvictorassignment.dto.DisplayableCommentDTO;
import com.pardalis.betvictorassignment.helper.enumeration.CommentAction;
import com.pardalis.betvictorassignment.service.CommentServiceImpl;
import com.pardalis.betvictorassignment.validator.CommentDTOValidatorImpl;
import com.pardalis.betvictorassignment.web.CommentWebSocketImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jms.core.JmsTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CommentWebSocketImplTest {
    @Mock
    private CommentServiceImpl commentServiceImpl;

    @Mock
    private CommentDTOValidatorImpl commentDTOValidator;

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
    public void sendCommentSuccessfull() throws Exception {

    }

    @Test
    public void sendCommentFailedJms() {

    }

    @Test
    public void sendCommentFailedValidation() {

    }
}
