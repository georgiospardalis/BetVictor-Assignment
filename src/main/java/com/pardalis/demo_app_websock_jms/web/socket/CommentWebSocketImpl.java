package com.pardalis.demo_app_websock_jms.web.socket;

import com.pardalis.demo_app_websock_jms.web.dto.CommentDTO;
import com.pardalis.demo_app_websock_jms.web.dto.DisplayableCommentDTO;
import com.pardalis.demo_app_websock_jms.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import java.util.List;

@Controller
public class CommentWebSocketImpl implements CommentWebSocket {
    private final static Logger LOGGER = LoggerFactory.getLogger(CommentWebSocketImpl.class);

    private CommentService commentService;

    @Autowired
    public CommentWebSocketImpl(CommentService commentService) {
        this.commentService = commentService;
    }

    @Override
    @SubscribeMapping("/comments")
    public List<DisplayableCommentDTO> onSubscribe() {
        return commentService.findAllPersistedComments();
    }

    @Override
    @MessageMapping("/comment")
    @SendToUser("/thread/comment_action")
    public String onMessage(@Valid CommentDTO commentDTO) throws RuntimeException {
        return commentService.sendCommentForReview(commentDTO);
    }

    @MessageExceptionHandler
    @SendToUser("/thread/error")
    public String onException(Exception e) {
        LOGGER.error("Error Handler Triggered", e);

        return e.getMessage();
    }
}