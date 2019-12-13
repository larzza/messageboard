package se.axeto.msgboardpoc.model;

import java.util.Objects;

public class MessageImpl implements Message {

    final String userId;
    final String messageId;
    final String message;

    public MessageImpl(String userId, String messageId, String message) {
        //TODO: Illegal argument instead? For null or empty string
        Objects.requireNonNull(userId);
        Objects.requireNonNull(messageId);
        Objects.requireNonNull(message); // Empty String ok

        this.userId = userId;
        this.messageId = messageId;
        this.message = message;
    }

    @Override
    public String getMessageId() {
        return messageId;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "MessageImpl{" +
                "userId='" + userId + '\'' +
                ", messageId='" + messageId + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
