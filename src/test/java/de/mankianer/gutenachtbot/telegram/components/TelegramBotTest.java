package de.mankianer.gutenachtbot.telegram.components;

import de.mankianer.gutenachtbot.telegram.TelegramService;
import de.mankianer.gutenachtbot.telegram.TelegramUserRepo;
import de.mankianer.gutenachtbot.telegram.models.TelegramUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class TelegramBotTest {

    @MockBean
    TelegramUserRepo telegramUserRepo;
    TelegramBot telegramBot;
    @MockBean
    TelegramUserComponent telegramUserComponent;
    @MockBean
    TelegramAdminComponent telegramAdminComponent;
    @MockBean
    TelegramCommandComponend telegramCommandComponend;
    @MockBean
    TelegramService telegramService;

    @BeforeEach
    void setUp() {
        telegramBot = new TelegramBot(telegramUserComponent, telegramCommandComponend);
        telegramBot.setTelegramService(telegramService);
    }

    @Test
    void onUpdateReceivedVerified() {
        var user = new TelegramUser();
        user.setState(TelegramUser.State.VERIFIED);
        when(telegramUserComponent.getUserByUpdate(any())).thenReturn(user);
        var update = new Update();
        update.setMessage(new Message());
        update.getMessage().setText("test");
        telegramBot.onUpdateReceived(update);
        verify(telegramCommandComponend, times(1)).getCommand(eq("test"), any());
    }

    @Test
    void onUpdateReceivedUnverified() {
        var user = new TelegramUser();
        user.setState(TelegramUser.State.UNVERIFIED);
        when(telegramUserComponent.getUserByUpdate(any())).thenReturn(user);
        var update = new Update();
        update.setMessage(new Message());
        update.getMessage().setText("test");
        telegramBot.onUpdateReceived(update);
        verify(telegramCommandComponend, times(0)).getCommand(any(), any());
        verify(telegramUserComponent, times(1)).handleNewUser(any());
    }


}