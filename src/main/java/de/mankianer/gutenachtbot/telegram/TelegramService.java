package de.mankianer.gutenachtbot.telegram;

import de.mankianer.gutenachtbot.telegram.commands.CommandInterface;
import de.mankianer.gutenachtbot.telegram.components.TelegramAdminComponent;
import de.mankianer.gutenachtbot.telegram.components.TelegramBot;
import de.mankianer.gutenachtbot.telegram.components.TelegramCommandComponend;
import de.mankianer.gutenachtbot.telegram.components.TelegramUserComponent;
import de.mankianer.gutenachtbot.telegram.models.TelegramUser;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Log4j2
@Component
public class TelegramService {

    private final TelegramBot telegramBot;
    private final TelegramAdminComponent telegramAdminComponent;
    private final TelegramCommandComponend telegramCommandComponend;


    public TelegramService(TelegramBot telegramBot, TelegramUserComponent telegramUserComponent, TelegramAdminComponent telegramAdminComponent, TelegramCommandComponend telegramCommandComponend) {
        this.telegramBot = telegramBot;
        this.telegramAdminComponent = telegramAdminComponent;
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
     *
     * @param message
     * @param user
     */
    public void sendMessage(String message, TelegramUser user) {
        SendMessage sendMessage = SendMessage.builder().chatId(String.valueOf(user.getChatId())).text(message).build();
        sendMessage(sendMessage);
    }

    /**
     * Executes the given message via TelegramBot
     *
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
     *
     * @param command
     */
    public void registerCommand(CommandInterface command) {
        log.info("Registering command: Admin: {} - {}", command.isAdminCommand(), command.getClass().getSimpleName());
        if(command.isAdminCommand()) {
            telegramAdminComponent.registerCommand(command);
        } else {
            telegramCommandComponend.registerCommand(command);
        }
    }

    /**
     * Sends a message to all admins
     * purpose: inform about errors, approvals, etc.
     *
     * @param message
     */
    public void sendMessagesToAdmins(String message) {
        telegramAdminComponent.sendMessageToAdmins(SendMessage.builder().chatId("").text(message).build());
    }

    /**
     * Get all commands for a user
     *
     * @param user
     * @return
     */
    public List<CommandInterface> getCommands(TelegramUser user) {
        return telegramCommandComponend.getCommands(user);
    }

    public String getWelcomeMessage(TelegramUser user) {
        StringBuilder message = new StringBuilder();
        message.append("Hallo %s, willkommen beim GuteNachtBot.%n".formatted(user.getFirstname()));
        if(user.isAdmin()) message.append("Du bist Admin!\n");
        message.append("Hier findest du eine Übersicht der Befehle, die du nutzen kannst:\n");
        List<CommandInterface> commands = getCommands(user);
        commands.sort((c1, c2) -> c1.getDescription().compareTo(c2.getDescription()));
        for (CommandInterface command : commands) {
            message.append("* %s%s%n".formatted(command.getDescription(), command.isAdminCommand() ? "¹" : ""));
        }
        if(user.isAdmin()) message.append("\n¹ - Adminbefehle");
        return message.toString();
    }
}
