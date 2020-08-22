package com.ag04.sbss.hackathon.app.services.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * A custom exception class describing the situation when the users request is being rejected.
 * @ResponseStatus annotation is present to set the
 * HTTP status to BAD_REQUEST when the exception is thrown.
 * Created by Vitomir M on 22.8.2020.
 */
@Getter
@Setter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequestDeniedException extends RuntimeException {

    public RequestDeniedException(String message) {
        super(message);
    }

}