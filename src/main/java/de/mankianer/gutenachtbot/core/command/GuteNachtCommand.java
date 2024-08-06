package de.mankianer.gutenachtbot.core.command;

import de.mankianer.gutenachtbot.core.GuteNachtConfigRepo;
import de.mankianer.gutenachtbot.core.GuteNachtService;
import de.mankianer.gutenachtbot.core.model.GuteNachtConfig;
import de.mankianer.gutenachtbot.telegram.CommandInterface;
import de.mankianer.gutenachtbot.telegram.TelegramService;
import de.mankianer.gutenachtbot.telegram.model.TelegramUser;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class GuteNachtCommand implements CommandInterface {

    private final TelegramService telegramService;
    private final GuteNachtService guteNachtService;
    private final GuteNachtConfigRepo guteNachtConfigRepo;

    public GuteNachtCommand(TelegramService telegramService, GuteNachtService guteNachtService, GuteNachtConfigRepo guteNachtConfigRepo) {
        this.telegramService = telegramService;
        this.guteNachtService = guteNachtService;
        this.guteNachtConfigRepo = guteNachtConfigRepo;
    }

    @PostConstruct
    public void init() {
        telegramService.registerCommand(this);
    }

    @Override
    public boolean matchesMessage(String message) {
        return message.toLowerCase().startsWith("gute nacht");
    }

    @Override
    public void onExecute(Update update, TelegramUser user) {
        guteNachtService.sendGuteNacht(user);
        guteNachtService.rescheduleGuteNacht(user);
    }
}
