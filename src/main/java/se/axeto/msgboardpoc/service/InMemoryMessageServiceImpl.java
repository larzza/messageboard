package se.axeto.msgboardpoc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.axeto.msgboardpoc.exceptions.MessageExistException;
import se.axeto.msgboardpoc.exceptions.MessageNotFoundException;
import se.axeto.msgboardpoc.exceptions.UserNotFoundException;
import se.axeto.msgboardpoc.model.Message;
import se.axeto.msgboardpoc.model.MessageImpl;

import java.util.*;

/**
 * An in-memory implementation of the MessageService interface.
 *
 * @see MessageService
 */

@Service
public class InMemoryMessageServiceImpl implements MessageService {
    final Logger logger = LoggerFactory.getLogger(InMemoryMessageServiceImpl.class);
    final private static String ENTER = "Enter";
    final private static String EXIT = "Exit";

    // Map to store users and their messages.
    // MAP <String, Map<String,Message>>
    // userid1 -> [messageID-1 -> message1], [messageID-2 -> message2]
    // userid2 -> [messageID-1 -> message1], [messageID-2 -> message2]
    private final Map<String, Map<String, Message>> userIdToMessageMap;

    @Autowired
    public InMemoryMessageServiceImpl(Map<String, Map<String, Message>> userIdToMessageMap) {
        logger.debug(ENTER, "Constructor", userIdToMessageMap);
        this.userIdToMessageMap = userIdToMessageMap;
        logger.debug(EXIT, userIdToMessageMap);
    }

    @Override
    public Collection<Message> getAllMessages() {
        final String METHOD_NAME = "getAllMessages";
        logger.debug(ENTER, METHOD_NAME);

        Collection<Message> allMessages = new ArrayList<>();
        for (Map<String, Message> msgIdToMessage : userIdToMessageMap.values()) {
            allMessages.addAll(msgIdToMessage.values());
        }

        logger.debug(EXIT, METHOD_NAME, allMessages);
        return allMessages;
    }

    @Override
    public void createMessage(Message message) throws MessageExistException {
        final String METHOD_NAME = "createMessage";
        logger.debug(ENTER, METHOD_NAME, message);

        Objects.requireNonNull(message, "Message must not be null");

        // Get all messages for the current user
        Map<String, Message> msgIdToMsgText = userIdToMessageMap.get(message.getUserId());
        if (Objects.isNull(msgIdToMsgText)) {
            // This is the first message this user creates
            // This simple implementation of the message service
            // "creates" the user and ads the new message

            // Create a map from message-id to message to store the
            // current users messages in.
            Map<String, Message> msgIdToMessages = new HashMap<>();

            // Add the message
            msgIdToMessages.put(message.getMessageId(),
                    new MessageImpl(message.getUserId(), message.getMessageId(), message.getMessage()));

            // Add the new user and the message map to the in-memory storage
            userIdToMessageMap.put(message.getUserId(), msgIdToMessages);

        } else {
            // The user exists
            if (Objects.nonNull(msgIdToMsgText.get(message.getMessageId()))) {
                // Can not create a new message for an existing message id
                String msg = String.format("A message with id %s already " +
                                "exist. Choose a unique message id and try again.",
                        message.getMessageId());
                throw new MessageExistException(message.getUserId(), message.getMessageId(), msg);
            } else {
                // Add the new message
                msgIdToMsgText.put(message.getMessageId(), message);
            }
        }

        logger.debug(EXIT, METHOD_NAME);
    }

    @Override
    public void updateMessage(Message message) throws UserNotFoundException, MessageNotFoundException {
        final String METHOD_NAME = "updateMessage";
        logger.debug(ENTER, METHOD_NAME, message);

        Objects.requireNonNull(message, "Message must not be null");

        // Check if user exists
        if (!userIdToMessageMap.containsKey(message.getUserId())) {
            // User does not exist
            String msg = String.format("Can not get message for non existing user: %s.",
                    message.getUserId());
            throw new UserNotFoundException(message.getUserId(), msg);
        }

        // Get user messages
        Map<String, Message> msgIdToMessage = userIdToMessageMap.get(message.getUserId());
        if (Objects.isNull(msgIdToMessage)) {
            // Can not update a non existing message
            String msg = String.format("The user %s has no messages to update.",
                    message.getUserId());
            throw new MessageNotFoundException(message.getUserId(), message.getUserId(), msg);
        }

        if (msgIdToMessage.containsKey(message.getMessageId())) {

            msgIdToMessage.replace(message.getMessageId(),
                    new MessageImpl(message.getUserId(), message.getMessageId(), message.getMessage()));

        } else {
            // Can not update an non existing message
            String msg = String.format("A message must exist to be updated. Message %s does not " +
                    "exist.", message.getMessageId());
            throw new MessageNotFoundException(message.getUserId(), message.getUserId(), msg);
        }

        logger.debug(EXIT, METHOD_NAME);
    }

    @Override
    public Message deleteMessage(Message message) throws UserNotFoundException, MessageNotFoundException {
        final String METHOD_NAME = "deleteMessage";
        logger.debug(ENTER, METHOD_NAME, message);

        Objects.requireNonNull(message, "Message must not be null");

        // Check if user exist
        if (!userIdToMessageMap.containsKey(message.getUserId())) {
            // User does not exist
            String msg = String.format("Can not delete message for non existing user: %s.",
                    message.getUserId());
            throw new UserNotFoundException(message.getUserId(), msg);
        }

        // Get user messages
        Map<String, Message> msgIdToMessage = userIdToMessageMap.get(message.getUserId());
        if (Objects.isNull(msgIdToMessage)) {
            // Can not delete message
            String msg = String.format("The user %s has no messages to delete.",
                    message.getUserId());
            throw new MessageNotFoundException(message.getUserId(), message.getUserId(), msg);
        }

        Message deletedMessage;
        if (msgIdToMessage.containsKey(message.getMessageId())) {
            deletedMessage = msgIdToMessage.remove(message.getMessageId());
        } else {
            // Can not update an non existing message
            String msg = String.format("A message must exist to be deleted. Message %s does not " +
                    "exist.", message.getMessageId());
            throw new MessageNotFoundException(message.getUserId(), message.getUserId(), msg);
        }

        logger.debug(EXIT, METHOD_NAME, deletedMessage);
        return deletedMessage;
    }

    @Override
    public Map<String, Message> getUserMessages(String userId) throws UserNotFoundException {
        final String METHOD_NAME = "getUserMessages";
        logger.debug(ENTER, METHOD_NAME, userId);

        Objects.requireNonNull(userId, "User must not be null");

        if (!userIdToMessageMap.containsKey(userId)) {
            throw new UserNotFoundException(userId, "The user does not exist.");
        }

        //TODO clone the map before returning it
        Map<String, Message> userMessages = userIdToMessageMap.get(userId);
        if (Objects.isNull(userMessages)) {
            userMessages = new HashMap<>();
        }

        logger.debug(EXIT, METHOD_NAME, userMessages);
        return userMessages;
    }

    @Override
    public Message getMessage(Message message) throws UserNotFoundException, MessageNotFoundException {
        final String METHOD_NAME = "getUserMessages";
        logger.debug(ENTER, METHOD_NAME, message);

        Objects.requireNonNull(message, "Message must not be null");

        String userId = message.getUserId();
        if (!userIdToMessageMap.containsKey(userId)) {
            throw new UserNotFoundException(userId, "The user does not exist.");
        }

        Map<String, Message> userMessages = userIdToMessageMap.get(userId);

        String messageId = message.getMessageId();
        if (Objects.isNull(userMessages)) {
            throw new MessageNotFoundException(userId, messageId, "The user has no messages.");
        }

        if (!userMessages.containsKey(messageId)) {
            throw new MessageNotFoundException(userId, messageId, "The message doesn't exist.");
        }

        Message theStoredMessage = userMessages.get(messageId);

        if (Objects.isNull(theStoredMessage)) {
            theStoredMessage = new MessageImpl(userId, messageId, "");
        }

        logger.debug(EXIT, METHOD_NAME, theStoredMessage);

        // Our message object is immutable so we can safely return it.
        return theStoredMessage;
    }

    @Override
    public Collection<String> getAllUsers() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Map<String, String>> getData() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<String> getUserMessageIds(String userId) {
        throw new UnsupportedOperationException();
    }

}