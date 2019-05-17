package com.deloitte.ddwatch.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ProcessingException extends DetailedException {
    public ProcessingException() {
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public ProcessingException(String msg) {
        this();
        this.message = msg;
    }
}
