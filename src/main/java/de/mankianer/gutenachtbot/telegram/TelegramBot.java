package de.mankianer.gutenachtbot.telegram;

import de.mankianer.gutenachtbot.telegram.model.TelegramUser;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Log4j2
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final TelegramUserRepo telegramUserRepo;
    private final TelegramUserComponent telegramUserComponent;
    @Setter
    private TelegramService telegramService;

    @Getter
    @Value("${telegram.bot.username}")
    private String botUsername;
    @Getter
    @Value("${telegram.bot.token}")
    private String botToken;


    public TelegramBot(TelegramUserRepo telegramUserRepo, TelegramUserComponent telegramUserComponent) {
        this.telegramUserRepo = telegramUserRepo;
        this.telegramUserComponent = telegramUserComponent;
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.debug("Update received: {}", update.getMessage().getChat().getUserName());
        TelegramUser user = telegramUserComponent.getUserByUpdate(update);
        if (user.isUnverified()) {
            telegramUserComponent.handleNewUser(user);
        } else {
            telegramService.sendMessage("Welcome back", user);
        }

    }
}
