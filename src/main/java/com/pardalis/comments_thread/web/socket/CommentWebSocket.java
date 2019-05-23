package com.pardalis.comments_thread.web.socket;

import com.pardalis.comments_thread.web.dto.CommentDTO;
import com.pardalis.comments_thread.web.dto.DisplayableCommentDTO;

import java.util.List;

public interface CommentWebSocket {
    List<DisplayableCommentDTO> onSubscribe();

    String onMessage(CommentDTO commentDTO) throws Exception;

    String onException(Exception e);
}