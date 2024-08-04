package de.mankianer.gutenachtbot.telegram.component;

import de.mankianer.gutenachtbot.telegram.TelegramService;
import de.mankianer.gutenachtbot.telegram.model.TelegramUser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j2
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final TelegramUserComponent telegramUserComponent;
    private final TelegramCommandComponend telegramCommandComponend;
    @Setter
    private TelegramService telegramService;

    @Getter
    @Value("${telegram.bot.username}")
    private String botUsername;
    @Getter
    @Value("${telegram.bot.token}")
    private String botToken;


    public TelegramBot(TelegramUserComponent telegramUserComponent, TelegramCommandComponend telegramCommandComponend) {
        this.telegramUserComponent = telegramUserComponent;
        this.telegramCommandComponend = telegramCommandComponend;
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.debug("Update received: {}", update.getMessage().getChat().getUserName());
        TelegramUser user = telegramUserComponent.getUserByUpdate(update);
        if (user.isUnverified()) {
            telegramUserComponent.handleNewUser(user);
        } else {
            telegramCommandComponend.getCommand(update.getMessage().getText(), user)
                    .ifPresent(command -> command.onExecute(update, user));
        }

    }
}
