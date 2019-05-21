package com.deloitte.ddwatch.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends DetailedException {
    public NotFoundException(String msg) {
        super(HttpStatus.BAD_REQUEST, msg);
    }
}
