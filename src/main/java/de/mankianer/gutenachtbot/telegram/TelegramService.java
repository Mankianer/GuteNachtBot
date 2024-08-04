package de.mankianer.gutenachtbot.telegram;

import de.mankianer.gutenachtbot.telegram.model.TelegramUser;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Optional;
import java.util.function.Supplier;

@Log4j2
@Component
public class TelegramService {

    private final TelegramBot telegramBot;
    private final TelegramUserComponent telegramUserComponent;

    public TelegramService(TelegramUserRepo telegramUserRepo, TelegramBot telegramBot, TelegramUserComponent telegramUserComponent) {
        this.telegramBot = telegramBot;
        this.telegramUserComponent = telegramUserComponent;
        this.telegramBot.setTelegramService(this);
        this.telegramUserComponent.setTelegramService(this);
        this.telegramUserComponent.setTelegramBot(telegramBot);
    }

    @PostConstruct
    public void init() {
        log.info("TelegramService started");
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(telegramBot);
        } catch (TelegramApiException e) {
            log.error("Error while registering bot");
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String message, TelegramUser user) {
        sendMessage(SendMessage.builder().chatId(String.valueOf(user.getChatId())).text(message).build());
    }

    public void sendMessage(SendMessage message) {
        try {
            telegramBot.execute(message);
        } catch (TelegramApiException e) {
            log.error("Error while sending message", e);
        }
    }
}
