package com.example.web_ide.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

@Getter
public class BaseException extends RuntimeException {
    private final ProblemDetail problemDetail;
    public BaseException(HttpStatus status, String message) {
        problemDetail=ProblemDetail.forStatusAndDetail(status,message);
    }
}
