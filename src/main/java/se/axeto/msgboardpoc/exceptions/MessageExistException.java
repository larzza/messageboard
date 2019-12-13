package se.axeto.msgboardpoc.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.CONFLICT, reason="Message already exists.")
public class MessageExistException extends MessageServiceException {

    private final String userId;
    private final String messageId;

    public MessageExistException(String userId, String messageId, String message) {
        super(message);
        this.userId = userId;
        this.messageId = messageId;
    }

    public String getUserId() {
        return userId;
    }

    public String getMessageId() {
        return messageId;
    }
}
