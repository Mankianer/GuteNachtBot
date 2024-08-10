package de.mankianer.gutenachtbot.telegram.commands;

import de.mankianer.gutenachtbot.telegram.TelegramService;
import de.mankianer.gutenachtbot.telegram.models.TelegramUser;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class WelcomeCommand extends SimpleCommand {

    private final TelegramService telegramService;

    public WelcomeCommand(TelegramService telegramService) {
        super("/welcome");
        this.telegramService = telegramService;
    }

    @Override
    public boolean matchesMessage(String message, TelegramUser user) {
        return message.startsWith("/welcome");
    }

    @Override
    public void onExecute(Update update, TelegramUser user) {
        telegramService.sendMessage("Hallo %s, willkommen beim GuteNachtBot.%nInfos:%s".formatted(user.getFirstname(), user), user);
    }
}
