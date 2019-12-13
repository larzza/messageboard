package se.axeto.msgboardpoc.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import se.axeto.msgboardpoc.exceptions.MessageExistException;
import se.axeto.msgboardpoc.exceptions.MessageNotFoundException;
import se.axeto.msgboardpoc.exceptions.UserNotFoundException;
import se.axeto.msgboardpoc.model.Message;
import se.axeto.msgboardpoc.model.MessageImpl;
import se.axeto.msgboardpoc.service.MessageService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;

/**
 * A client can create a message in the service
 * A client can modify their own messages
 * A client can delete their own messages
 * A client can view all messages in the service
 */

@RequestMapping("api/v1/message")
@RestController
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping(path = "all")
    public Collection<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    @PostMapping
    public void addMessage(@Valid @NonNull @RequestBody MessageImpl message) throws MessageExistException {
        messageService.createMessage(message);
    }

    @PutMapping
    public void updateMessage(@Valid @NonNull @RequestBody MessageImpl message) throws UserNotFoundException, MessageNotFoundException {
        messageService.updateMessage(message);
    }

    @DeleteMapping
    public void deleteMessage(@RequestBody MessageImpl message) throws UserNotFoundException, MessageNotFoundException {
        messageService.deleteMessage(message);
    }

    @GetMapping(path = "{user}")
    public Map<String, Message> getUserMessages(@PathVariable("user") String userId) throws UserNotFoundException {
        Map<String, Message> result;
        try {
            result = messageService.getUserMessages(userId);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                              "The user " + userId + " doesn't exist.", e);
        }
        return result;
    }

    @GetMapping(path = "{user}/{messageId}")
    public Message getMessage(@PathVariable("user")  String userId, @PathVariable("messageId") String messageId)
            throws UserNotFoundException, MessageNotFoundException {
        return messageService.getMessage(new MessageImpl(userId, messageId, ""));
    }

    @GetMapping(path = "ids/{user}")
    public Collection<String> getUserMessageIds(@PathVariable("user")  String userId) {
        Collection<String> userMessageIds;
        //Respond with a not implemented status
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED,
                "The endpoint to get a list of " +
                "the message ids for a user is yet to be implemented");

//        //TODO: implement getUserMessageIds
//        try {
//            userMessageIds = messageService.getUserMessageIds(userId);
//        }
//        catch (UserNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
//                "The user " + userId + " doesn't exist.", e);
//        }

        //return userMessageIds;
    }

}



