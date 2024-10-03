package com.example.web_ide.exception;

import org.springframework.http.HttpStatus;

public class RedisDataAccessException extends BaseException {

    static final HttpStatus STATUS=HttpStatus.INTERNAL_SERVER_ERROR;
    static final String MESSAGE="redis 데이터 접근 중 문제가 발생했습니다.";

    public RedisDataAccessException() {
        super(STATUS,MESSAGE);
    }
}
