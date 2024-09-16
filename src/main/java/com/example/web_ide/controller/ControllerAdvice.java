package com.example.web_ide.controller;
import com.example.web_ide.exception.BaseException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<?> handleDataAccessException(DataAccessException e) {
        return ResponseEntity.internalServerError().body("데이터베이스에서 에러가 발생했습니다.");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException e){
        StringBuilder message=new StringBuilder();
        e.getConstraintViolations().forEach(violation->{
            message.append(violation.getMessage()).append('\n');
        });
        ProblemDetail problemDetail=ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,message.toString());
        return ErrorResponse.builder(e,problemDetail).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        ProblemDetail problemDetail=e.getBody();
        StringBuilder message=new StringBuilder();
        e.getBindingResult().getFieldErrors().forEach(error->{
          message.append(error.getDefaultMessage()).append("\n");
        });
        problemDetail.setDetail(message.toString());
        return ErrorResponse.builder(e,problemDetail).build();

    }

    @ExceptionHandler(BaseException.class)
    public ErrorResponse ExceptionBase(BaseException e){
        return ErrorResponse.builder(e,e.getProblemDetail()).build();
    }


//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> handleAllException(Exception e) {
//        return ResponseEntity.internalServerError().body("알 수 없는 에러가 발생했습니다.");
//    }
}
