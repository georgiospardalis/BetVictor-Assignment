package com.pardalis.demo_app_websock_jms.web.socket;

import com.pardalis.demo_app_websock_jms.web.dto.CommentDTO;
import com.pardalis.demo_app_websock_jms.web.dto.DisplayableCommentDTO;

import java.util.List;

public interface CommentWebSocket {
    List<DisplayableCommentDTO> onSubscribe();

    String onMessage(CommentDTO commentDTO) throws Exception;

    String onException(Exception e);
}