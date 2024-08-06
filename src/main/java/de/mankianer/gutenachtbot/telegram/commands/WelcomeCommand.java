package de.mankianer.gutenachtbot.telegram.commands;

import de.mankianer.gutenachtbot.telegram.CommandInterface;
import de.mankianer.gutenachtbot.telegram.TelegramService;
import de.mankianer.gutenachtbot.telegram.models.TelegramUser;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class WelcomeCommand implements CommandInterface {

    private final TelegramService telegramService;

    public WelcomeCommand(TelegramService telegramService) {
        this.telegramService = telegramService;
    }

    @PostConstruct
    public void init() {
        telegramService.registerCommand(this);
    }

    @Override
    public boolean matchesMessage(String message) {
        return message.startsWith("/welcome");
    }

    @Override
    public void onExecute(Update update, TelegramUser user) {
        telegramService.sendMessage("Hallo %s, willkommen beim GuteNachtBot.%nInfos:%s".formatted(user.getFirstname(), user), user);
    }
}
