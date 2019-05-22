package com.pardalis.betvictorassignment.web.socket;

import com.pardalis.betvictorassignment.web.dto.CommentDTO;
import com.pardalis.betvictorassignment.web.dto.DisplayableCommentDTO;

import java.util.List;

public interface CommentWebSocket {
    List<DisplayableCommentDTO> onSubscribe();

    String onMessage(CommentDTO commentDTO) throws Exception;

    String onException(Exception e);
}