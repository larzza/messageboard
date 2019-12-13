package se.axeto.msgboardpoc.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="The request couldn't be processed.")
public class MessageServiceException extends Exception {

    public MessageServiceException(String message) {
        super(message);
    }

}
