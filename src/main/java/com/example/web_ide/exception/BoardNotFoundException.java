package com.example.web_ide.exception;

import org.springframework.http.HttpStatus;

public class BoardNotFoundException extends BaseException {
    static final HttpStatus STATUS=HttpStatus.NOT_FOUND;
    static final String MESSAGE="게시글을 찾을 수 없습니다.";

    public BoardNotFoundException() {
        super(STATUS,MESSAGE);
    }
}
