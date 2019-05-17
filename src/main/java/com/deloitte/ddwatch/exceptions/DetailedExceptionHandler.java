package com.deloitte.ddwatch.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class DetailedExceptionHandler {

    @ExceptionHandler(DetailedException.class)
    public ResponseEntity<String> handle(DetailedException e) {
        log.error("DetailedExceptionHandler got: {} -> {}",
                e.getClass().getSimpleName(),
                e.getMessage()
        );

        return new ResponseEntity<>(e.getMessage(), e.getStatus());
    }
}
