package de.mankianer.gutenachtbot.telegram.components;

import de.mankianer.gutenachtbot.telegram.commands.CommandInterface;
import de.mankianer.gutenachtbot.telegram.models.TelegramUser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class TelegramCommandComponend {

    private List<CommandInterface> commands;

    private final TelegramAdminComponent telegramAdminComponent;

    public TelegramCommandComponend(TelegramAdminComponent telegramAdminComponent) {
        this.telegramAdminComponent = telegramAdminComponent;
        commands = new ArrayList<>();
    }

    public void registerCommand(CommandInterface command) {
        commands.add(command);
    }

    /**
     * Get command by message and user
     * if user is admin, check admin commands first
     *
     * @param message
     * @return
     */
    public Optional<CommandInterface> getCommand(String message, TelegramUser user) {
        if (user.isAdmin()) {
            Optional<CommandInterface> adminCommand = telegramAdminComponent.getAdminCommands().stream().filter(command -> command.matchesMessage(message, user)).findFirst();
            if (adminCommand.isPresent()) {
                return adminCommand;
            }
        }
        return commands.stream().filter(command -> command.matchesMessage(message, user)).findFirst();
    }

    public List<CommandInterface> getCommands(TelegramUser user) {
        List<CommandInterface> commands = new ArrayList<>(this.commands);
        if(user.isAdmin()) {
            commands.addAll(this.telegramAdminComponent.getAdminCommands());
        }
        return commands;
    }
}
