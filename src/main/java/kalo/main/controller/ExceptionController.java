package kalo.main.controller;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ExceptionResponse invalidRequst(MethodArgumentNotValidException e) {
        return ExceptionResponse.builder().message("잘못된 요청입니다.").build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public ExceptionResponse invalidRequst(MissingServletRequestParameterException e) {
        return ExceptionResponse.builder().message("잘못된 요청입니다.").build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BasicException.class)
    @ResponseBody
    public ExceptionResponse serverException(BasicException e) {
        return ExceptionResponse.builder().message(e.getMessage()).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    @ResponseBody
    public ExceptionResponse invalidRequst(InvalidDataAccessApiUsageException e) {
        return ExceptionResponse.builder().message("잘못된 요청입니다.").build();
    }

}
