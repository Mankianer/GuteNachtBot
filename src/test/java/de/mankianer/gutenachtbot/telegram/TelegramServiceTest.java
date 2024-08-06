package de.mankianer.gutenachtbot.telegram;

import de.mankianer.gutenachtbot.telegram.components.TelegramAdminComponent;
import de.mankianer.gutenachtbot.telegram.components.TelegramBot;
import de.mankianer.gutenachtbot.telegram.components.TelegramCommandComponend;
import de.mankianer.gutenachtbot.telegram.components.TelegramUserComponent;
import de.mankianer.gutenachtbot.telegram.models.TelegramUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TelegramServiceTest {

    @Mock
    TelegramUserRepo telegramUserRepo;
    @Mock
    TelegramBot telegramBot;
    @Mock
    TelegramUserComponent telegramUserComponent;
    @Mock
    TelegramAdminComponent telegramAdminComponent;
    @Mock
    TelegramCommandComponend telegramCommandComponend;

    TelegramService telegramService;

    @Captor
    ArgumentCaptor<SendMessage> argCaptor;

    @BeforeEach
    void setUp() {
        telegramService = new TelegramService(telegramBot, telegramUserComponent, telegramAdminComponent, telegramCommandComponend);
    }

    /**
     * Simple test to check if the sendMessage method is working and checks the right chatId and Message Text
     */
    @Test
    void sendMessage() throws TelegramApiException {
        TelegramUser user = new TelegramUser();
        user.setChatId(1234567890L);
        telegramService.sendMessage("Test", user);
        Mockito.verify(telegramBot).execute(argCaptor.capture());
        assertEquals("Test", argCaptor.getValue().getText());
        assertEquals("1234567890", argCaptor.getValue().getChatId());
    }

    @Test
    void testSendMessage() throws TelegramApiException {
        SendMessage testMessage = SendMessage.builder().chatId("1234567890").text("Test").build();
        telegramService.sendMessage(testMessage);
        Mockito.verify(telegramBot).execute(argCaptor.capture());
    }
}
