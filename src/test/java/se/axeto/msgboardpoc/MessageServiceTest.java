package se.axeto.msgboardpoc;

import org.junit.jupiter.api.*;
import se.axeto.msgboardpoc.exceptions.MessageExistException;
import se.axeto.msgboardpoc.exceptions.MessageNotFoundException;
import se.axeto.msgboardpoc.exceptions.UserNotFoundException;
import se.axeto.msgboardpoc.model.Message;
import se.axeto.msgboardpoc.model.MessageImpl;
import se.axeto.msgboardpoc.service.InMemoryMessageServiceImpl;
import se.axeto.msgboardpoc.service.MessageService;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MessageServiceTest {

    // Users
    private static final String KARL_OVE_KNAUSGÅRD = "Karl-Ove Knausgård";
    private static final String OLGA_TOKARCZUK = "Olga Tokarczuk";
    private static final String DORIS_LESSING = "Doris Lessing";

    // Message ids
    private static final String MY_STRUGGLE_1 = "My struggle 1";
    private static final String MY_STRUGGLE_2 = "My struggle 2";
    private static final String MY_STRUGGLE_3 = "My struggle 3";
    private static final String MY_STRUGGLE_4 = "My struggle 4";
    private static final String MY_STRUGGLE_5 = "My struggle 5";
    private static final String MY_STRUGGLE_6 = "My struggle 6";
    private static final String MARTHA_QUEST = "Martha Quest";

    // Mesages
    private static final String MY_STRUGGLE_1_MESSAGE =
            "My struggle 1 - the first novel in Karl-Ove Knausgårds tale about his life.";
    private static final String MARTHA_QUEST_MESSAGE =
            "Martha Quest is the first novel in the series The Children of Violence.";

    private static MessageService messageService;

    @BeforeEach
    void beforeEach() {

        Map<String, Map<String, Message>> testDataAuthorToBooksMap = new HashMap<>();

        Map<String, Message> karlOvesBooks = new HashMap<>();
        karlOvesBooks.put(MY_STRUGGLE_1,
                new MessageImpl(KARL_OVE_KNAUSGÅRD, MY_STRUGGLE_1, MY_STRUGGLE_1_MESSAGE));
        karlOvesBooks.put(MY_STRUGGLE_2,
                new MessageImpl(KARL_OVE_KNAUSGÅRD, MY_STRUGGLE_2,
                        "My struggle 2 - the second novel in Karl-Ove Knausgårds tale about his life."));
        karlOvesBooks.put(MY_STRUGGLE_3,
                new MessageImpl(KARL_OVE_KNAUSGÅRD, MY_STRUGGLE_3,
                        "My struggle 3 - the third novel in Karl-Ove Knausgårds tale about his life."));
        karlOvesBooks.put(MY_STRUGGLE_4,
                new MessageImpl(KARL_OVE_KNAUSGÅRD, MY_STRUGGLE_4,
                        "My struggle 4 - the fourth novel in Karl-Ove Knausgårds tale about his life."));
        karlOvesBooks.put(MY_STRUGGLE_5,
                new MessageImpl(KARL_OVE_KNAUSGÅRD, MY_STRUGGLE_5,
                        "My struggle 5 - the fifth novel in Karl-Ove Knausgårds tale about his life."));
        karlOvesBooks.put(MY_STRUGGLE_6,
                new MessageImpl(KARL_OVE_KNAUSGÅRD, MY_STRUGGLE_6,
                        "My struggle 6 - the sixth novel in Karl-Ove Knausgårds tale about his life."));

        testDataAuthorToBooksMap.put(KARL_OVE_KNAUSGÅRD, karlOvesBooks);

        Map<String, Message> dorisBooks = new HashMap<>();
        dorisBooks.put(MARTHA_QUEST, new MessageImpl(DORIS_LESSING, MARTHA_QUEST, MARTHA_QUEST_MESSAGE));
        dorisBooks.put("A Proper Marriage",
                new MessageImpl(DORIS_LESSING, "A Proper Marriage",
                        "A Proper Marriage is the second novel in the series The Children of Violence."));
        dorisBooks.put("A Ripple from the Storm",
                new MessageImpl(DORIS_LESSING, "A Ripple from the Storm",
                "A Ripple from the Storm is the third novel in the series The Children of Violence."));
        dorisBooks.put("Landlocked ",
                new MessageImpl(DORIS_LESSING, "Landlocked",
                "Landlocked  is the fourth novel in the series The Children of Violence."));
        dorisBooks.put("The Four-Gated City",
                new MessageImpl(DORIS_LESSING, "The Four-Gated City",
                "The Four-Gated City is the fifth novel in the series The Children of Violence."));
        testDataAuthorToBooksMap.put(DORIS_LESSING, dorisBooks);

        Map<String, Message> olgaBooks = new HashMap<>();
        olgaBooks.put("Nobel",
                new MessageImpl(OLGA_TOKARCZUK, "Nobel",
                "In 2019 Olga Tokarczuk was rewarded the 2018 Nobel price in litterature."));
        testDataAuthorToBooksMap.put(OLGA_TOKARCZUK, olgaBooks);

        MessageServiceTest.messageService = new InMemoryMessageServiceImpl(testDataAuthorToBooksMap);

    }

    @AfterEach
    void tearDown() {
        MessageServiceTest.messageService = null;
    }

    @Test
    void getAllMessages() {
        Assertions.assertEquals(12, this.messageService.getAllMessages().size());
    }

    @Test
    void createMessageExpectSuccess() {

        // Create message
        final String newMessage = "A new message about a book";
        final String messageId = "aMessageId";
        assertDoesNotThrow(() -> {
            this.messageService.createMessage(
                    new MessageImpl(KARL_OVE_KNAUSGÅRD, messageId, newMessage));
        });

        // Get the created message
        String createdMessage =
            assertDoesNotThrow(() -> {
                return this.messageService.getMessage(
                    new MessageImpl(KARL_OVE_KNAUSGÅRD, messageId, "")).getMessage();
            });

        // Verify the created message
        Assertions.assertEquals(newMessage, createdMessage);

    }

    @Test
    void createMessageExpectMessageExist() {
        // Create message
        String newMessage = "A new message about a book";
        assertThrows(MessageExistException.class, () -> {
            this.messageService.createMessage(
                    new MessageImpl(KARL_OVE_KNAUSGÅRD, MY_STRUGGLE_1, newMessage));
        });
    }

    @Test
    void getMessage() {
        // Get a message
        String message =
            assertDoesNotThrow(() -> {
                return this.messageService.getMessage(
                        new MessageImpl(DORIS_LESSING, MARTHA_QUEST, "")).getMessage();
            });

        // Verify the created message
        Assertions.assertEquals(MARTHA_QUEST_MESSAGE, message);
    }

    @Test
    void getMessageUserNotFound() {
        // Get message for non existing user
        assertThrows(UserNotFoundException.class, () -> {
            this.messageService.getMessage(
                    new MessageImpl("Dlorisz", MARTHA_QUEST, ""));
        });
    }

    @Test
    void getMessageMessageNotFound() {
        // User exist, message does not
        assertThrows(MessageNotFoundException.class, () -> {
            this.messageService.getMessage(
                    new MessageImpl(DORIS_LESSING, "MarthaKnausgard", ""));
        });
    }

    @Test
    void updateMessage() {
        // Get message from service
        String myStruggle1Msg =
                assertDoesNotThrow(() -> {
                    return this.messageService.getMessage(
                            new MessageImpl(KARL_OVE_KNAUSGÅRD, MY_STRUGGLE_1, "")).getMessage();
                });

        // Update message
        String newMessage = "A new message about a book";
        assertDoesNotThrow(() -> {
            this.messageService.updateMessage(
                new MessageImpl(KARL_OVE_KNAUSGÅRD, MY_STRUGGLE_1, newMessage));
        });

        // Get the updated message
        String updatedMessage =
                assertDoesNotThrow(() -> { return this.messageService.getMessage(
                        new MessageImpl(KARL_OVE_KNAUSGÅRD, MY_STRUGGLE_1, "")).getMessage(); });

        // Verify the updated message
        Assertions.assertEquals(newMessage, updatedMessage);
    }

    @Test
    void updateNonExistingMessageExpectMessageNotFound() {
        // Try to update a non existing message
        String newMessage = "A new message about a book";
        assertThrows(MessageNotFoundException.class, () -> {
            this.messageService.updateMessage(
                new MessageImpl(KARL_OVE_KNAUSGÅRD, "InvalidMessageId", newMessage));
        });
    }

    @Test
    void deleteMessage() {
        // Get message from service
        String myStruggle1Msg =
                assertDoesNotThrow(() -> { return this.messageService.getMessage(
                        new MessageImpl(KARL_OVE_KNAUSGÅRD, MY_STRUGGLE_1, "")).getMessage();});

        // Delete message
        String myStruggleOriginalMsg =
                assertDoesNotThrow(() -> {
                    return this.messageService.deleteMessage(
                        new MessageImpl(KARL_OVE_KNAUSGÅRD, MY_STRUGGLE_1, "")).getMessage();});

        // Verify the returned deleted message
        Assertions.assertEquals(MY_STRUGGLE_1_MESSAGE, myStruggleOriginalMsg);

        // Try getting the deleted message
        assertThrows(MessageNotFoundException.class,
                    () -> { this.messageService.getMessage(new MessageImpl(KARL_OVE_KNAUSGÅRD, MY_STRUGGLE_1, "")); }
        );

    }

}