package com.example.junittest.web.handler;

import com.example.junittest.web.dto.response.CommonResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> apiException(RuntimeException e) {

        return new ResponseEntity<>(CommonResponseDto.builder().code(-1).msg(e.getMessage()).build(), HttpStatus.BAD_REQUEST);

    }
}
