package de.mankianer.gutenachtbot.telegram;

import de.mankianer.gutenachtbot.telegram.components.TelegramAdminComponent;
import de.mankianer.gutenachtbot.telegram.components.TelegramBot;
import de.mankianer.gutenachtbot.telegram.components.TelegramCommandComponend;
import de.mankianer.gutenachtbot.telegram.components.TelegramUserComponent;
import de.mankianer.gutenachtbot.telegram.models.TelegramUser;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Log4j2
@Component
public class TelegramService {

    private final TelegramBot telegramBot;
    private final TelegramCommandComponend telegramCommandComponend;


    public TelegramService(TelegramBot telegramBot, TelegramUserComponent telegramUserComponent, TelegramAdminComponent telegramAdminComponent, TelegramCommandComponend telegramCommandComponend) {
        this.telegramBot = telegramBot;
        this.telegramCommandComponend = telegramCommandComponend;
        this.telegramBot.setTelegramService(this);
        telegramUserComponent.setTelegramService(this);
        telegramAdminComponent.setTelegramService(this);
    }

    @PostConstruct
    public void init() {
        log.info("TelegramService started");
        telegramBot.registerBot();
    }

    /**
     * Sends a message to the user
     * convert the message and user to a SendMessage object
     * @param message
     * @param user
     */
    public void sendMessage(String message, TelegramUser user) {
        sendMessage(SendMessage.builder().chatId(String.valueOf(user.getChatId())).text(message).build());
    }

    /**
     * Executes the given message via TelegramBot
     * @param message
     */
    public void sendMessage(SendMessage message) {
        try {
            telegramBot.execute(message);
        } catch (TelegramApiException e) {
            log.error("Error while sending message", e);
        }
    }

    /**
     * Registers a command for all TelegramUsers
     * @param command
     */
    public void registerCommand(CommandInterface command) {
        telegramCommandComponend.registerCommand(command);
    }
}
