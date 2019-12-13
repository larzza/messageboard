package se.axeto.msgboardpoc.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No such message.")
public class MessageNotFoundException extends MessageServiceException {

    private String userId;
    private String messageId;

    public MessageNotFoundException(String userId, String messageId, String message) {
        super(message);
        this.userId = userId;
        this.messageId = messageId;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getUserId() {
        return userId;
    }
}
