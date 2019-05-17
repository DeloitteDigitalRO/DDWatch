package com.deloitte.ddwatch.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data @NoArgsConstructor @AllArgsConstructor
public abstract class DetailedException extends RuntimeException {
    protected HttpStatus status;
    protected String message;

    public DetailedException(HttpStatus status) {
        this.status = status;
    }
}
