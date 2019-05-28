package com.deloitte.ddwatch.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@ControllerAdvice
public class DetailedExceptionHandler {

    @ExceptionHandler(ExcelProcessingException.class)
    public ResponseEntity<String> handle(ExcelProcessingException e) {
        log.error("Excel processing exception {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handle(ResponseStatusException e) {
        log.error("ResponseStatusException: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), e.getStatus());
    }
}
