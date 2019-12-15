package se.axeto.msgboardpoc.model;

/**
 * Representation of a message.
 */
public interface Message {

    String getMessageId();

    String getMessage();

    String getUserId();
}
