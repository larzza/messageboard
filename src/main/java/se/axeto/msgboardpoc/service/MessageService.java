package se.axeto.msgboardpoc.service;

import se.axeto.msgboardpoc.exceptions.MessageExistException;
import se.axeto.msgboardpoc.exceptions.MessageNotFoundException;
import se.axeto.msgboardpoc.exceptions.UserNotFoundException;
import se.axeto.msgboardpoc.model.Message;

import java.util.Collection;
import java.util.Map;

/**
 * An interface specifying the api of the message service.
 *
 * This is an naive approach that use strings to identify
 * both a user and a message.
 *
 * <p>
 * A message (<code>Message</code>) is identified by a message id
 * (<code>String</code>) and are grouped per user (<code>String</code>).
 *</p>
 *
 * <br>
 * <table>
 *     <caption>Example data structure</caption>
 *     <tr>
 *         <th>User</th>
 *         <th>Messages (Message-id -&gt; message)</th>
 *     </tr>
 *     <tr>
 *         <td>"Karl-Ove" : </td>
 *         <td> ["Bicycle": "I love to ride my bike", "Football" : "I think football is fun"]</td>
 *     </tr>
 *     <tr>
 *         <td>"Doris" : </td>
 *         <td> ["Nobel": "I've got the Nobel price 2007", "Football" : "I couldn't care less about football"]</td>
 *     </tr>
 * </table>
 *
 *
 */
public interface MessageService {

    /**
     * Get all messages as a list of message objects.
     *
     * @return A list of all messages.
     */
    public Collection<Message> getAllMessages();

    /**
     * Create a new message for a user.
     *
     * @param message the message to create.
     */
    void createMessage(Message message) throws MessageExistException;

    /**
     * Updates an existing message for a user.
     *
     * @param message the message to update.
     */
    void updateMessage(Message message) throws UserNotFoundException, MessageNotFoundException;

    /**
     * Delete a message for a user.
     *
     * @param message the message to delete.
     * @return The deleted message.
     */
    Message deleteMessage(Message message) throws UserNotFoundException, MessageNotFoundException;

    /**
     * Get a list of all users.
     *
     * @return A list of users.
     */
    Collection<String> getAllUsers();

    /**
     *  Get all messages including user-name/id:s and message id:s.
     *
     * @return The data in the service represented as a map from user-id
     * to the users messages. The user messages is represented as a map
     * from message-id to message.
     */
    Map<String, Map<String, String>> getData();

    /**
     *  Get all the messages of a user.
     *
     * @param user name/id as a <code>String</code>.
     * @return A map from message-id to message.
     */
    Map<String, Message> getUserMessages(String user) throws UserNotFoundException;

    /**
     * Get a user message.
     *
     * @param message a message object with user and message-id.
     * @return The message.
.     */
    Message getMessage(Message message) throws UserNotFoundException, MessageNotFoundException;

    /**
     * Get the ids of all messages for a specific user.
     *
     * @param user user name/id as a <code>String</code>.
     * @return A list of the users message ids.
     */
    Collection<String> getUserMessageIds(String user);


}
