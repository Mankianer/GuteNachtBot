package de.mankianer.gutenachtbot.telegram.commands;

import de.mankianer.gutenachtbot.telegram.TelegramService;
import de.mankianer.gutenachtbot.telegram.models.TelegramUser;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class WelcomeCommand extends SimpleCommand {

    private final TelegramService telegramService;

    public WelcomeCommand(TelegramService telegramService) {
        super("/welcome", "Ausgabe der Willkommensnachricht mit Infos");
        this.telegramService = telegramService;
    }

    @Override
    public boolean matchesMessage(String message, TelegramUser user) {
        return message.startsWith("/welcome");
    }

    @Override
    public void onExecute(Update update, TelegramUser user) {
        StringBuilder message = new StringBuilder();
        message.append("Hallo %s, willkommen beim GuteNachtBot.%n".formatted(user.getFirstname()));
        if(user.isAdmin()) message.append("Du bist Admin!\n");
        message.append("Hier findest du eine Übersicht der Befehle, die du nutzen kannst:\n");
        List<CommandInterface> commands = telegramService.getCommands(user);
        commands.sort((c1, c2) -> c1.getDescription().compareTo(c2.getDescription()));
        for (CommandInterface command : commands) {
            message.append("* %s%s%n".formatted(command.getDescription(), command.isAdminCommand() ? "¹" : ""));
        }
        if(user.isAdmin()) message.append("\n¹ - Adminbefehle");
        telegramService.sendMessage(message.toString(), user);
    }
}
