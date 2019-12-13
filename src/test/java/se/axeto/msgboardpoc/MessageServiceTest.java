package se.axeto.msgboardpoc;

import org.junit.jupiter.api.*;
import se.axeto.msgboardpoc.exceptions.MessageNotFoundException;
import se.axeto.msgboardpoc.model.Message;
import se.axeto.msgboardpoc.model.MessageImpl;
import se.axeto.msgboardpoc.service.InMemoryMessageServiceImpl;
import se.axeto.msgboardpoc.service.MessageService;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MessageServiceTest {

    private static final String KARL_OVE_KNAUSGÅRD = "Karl-Ove Knausgård";
    private static final String OLGA_TOKARCZUK = "Olga Tokarczuk";
    private static final String DORIS_LESSING = "Doris Lessing";
    private static final String MY_STRUGGLE_1 = "My struggle 1";
    private static final String MY_STRUGGLE_2 = "My struggle 2";
    private static final String MY_STRUGGLE_3 = "My struggle 3";
    private static final String MY_STRUGGLE_4 = "My struggle 4";
    private static final String MY_STRUGGLE_5 = "My struggle 5";
    private static final String MY_STRUGGLE_6 = "My struggle 6";
    private static final String MY_STRUGGLE_1_MESSAGE =
            "My struggle 1 - the first novel in Karl-Ove Knausgårds tale about his life.";
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
        dorisBooks.put("Martha Quest",
                new MessageImpl(DORIS_LESSING, "Martha Quest",
                        "Martha Quest is the first novel in the series The Children of Violence."));
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
    void createMessage() {

    }

    @Test
    void updateMessage() {
        // Get message from service
        String myStruggle1Msg =
                assertDoesNotThrow(() ->
                { return this.messageService.getMessage(
                            new MessageImpl(KARL_OVE_KNAUSGÅRD, MY_STRUGGLE_1, "")).getMessage();});

        // Update message
        String newMessage = "A new message about a book";
        assertDoesNotThrow(() ->
        { this.messageService.updateMessage(
                new MessageImpl(KARL_OVE_KNAUSGÅRD, MY_STRUGGLE_1, newMessage));});

        // Get the updated message
        String updatedMessage =
                assertDoesNotThrow(() -> { return this.messageService.getMessage(
                        new MessageImpl(KARL_OVE_KNAUSGÅRD, MY_STRUGGLE_1, "")).getMessage(); });

        // Verify the updated message
        Assertions.assertEquals(newMessage, updatedMessage);
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

    @Test
    void getAllUsers() {
    }

    @Test
    void getData() {
    }

    @Test
    void getUserMessages() {

    }

    @Test
    void getMessage() {
    }

    @Test
    void getUserMessageIds() {
    }
}