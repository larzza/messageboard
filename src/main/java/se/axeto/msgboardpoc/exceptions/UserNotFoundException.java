package se.axeto.msgboardpoc.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception to throw when an invalid user has been requested.
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No such user.")
public class UserNotFoundException extends MessageServiceException {

    private String user;

    public UserNotFoundException(String user, String message) {
        super(message);
        this.user = user;
    }

    public String getUser() {
        return user;
    }

}
