package com.pardalis.betvictorassignment.web;

import com.pardalis.betvictorassignment.dto.CommentDTO;
import com.pardalis.betvictorassignment.dto.DisplayableCommentDTO;
import com.pardalis.betvictorassignment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CommentWebSocketImpl implements CommentWebSocket {
    @Autowired
    CommentService commentService;

    @Override
    @SubscribeMapping("/thread/comments")
    public List<DisplayableCommentDTO> onSubscribe() {
        return commentService.findAllPersistedComments();
    }

    @Override
    @MessageMapping("/comment")
    public String onMessage(CommentDTO commentDTO) {
        return null;
    }
}