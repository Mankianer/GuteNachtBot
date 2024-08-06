package de.mankianer.gutenachtbot.core.commands;

import de.mankianer.gutenachtbot.core.GuteNachtConfigRepo;
import de.mankianer.gutenachtbot.core.GuteNachtService;
import de.mankianer.gutenachtbot.telegram.CommandInterface;
import de.mankianer.gutenachtbot.telegram.TelegramService;
import de.mankianer.gutenachtbot.telegram.models.TelegramUser;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class GuteNachtCommand implements CommandInterface {

    private final GuteNachtService guteNachtService;
    private final GuteNachtConfigRepo guteNachtConfigRepo;

    public GuteNachtCommand(GuteNachtService guteNachtService, GuteNachtConfigRepo guteNachtConfigRepo) {
        this.guteNachtService = guteNachtService;
        this.guteNachtConfigRepo = guteNachtConfigRepo;
    }

    @Override
    public boolean matchesMessage(String message, TelegramUser user) {
        return message.toLowerCase().startsWith("gute nacht");
    }

    @Override
    public void onExecute(Update update, TelegramUser user) {
        guteNachtService.sendGuteNacht(user);
        guteNachtService.rescheduleGuteNacht(user);
    }
}
