package de.mankianer.gutenachtbot.telegram.components;

import de.mankianer.gutenachtbot.telegram.TelegramService;
import de.mankianer.gutenachtbot.telegram.models.TelegramUser;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

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

    public void registerBot() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(this);
        } catch (TelegramApiException e) {
            log.error("Error while registering bot");
            throw new RuntimeException(e);
        }
    }


    @Override
    public void onUpdateReceived(Update update) {
        if(log.isDebugEnabled() && update.getMessage() != null && update.getMessage().getChat() != null) log.debug("Update received: {}", update.getMessage().getChat().getUserName());
        TelegramUser user = telegramUserComponent.getUserByUpdate(update);
        if (user.isUnverified()) {
            telegramUserComponent.handleNewUser(user);
        } else {
            telegramCommandComponend.getCommand(update.getMessage().getText(), user)
                    .ifPresentOrElse(command -> command.onExecute(update, user),
                            () -> telegramService.sendMessage("Command not found", user));
        }

    }

}
