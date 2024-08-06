package de.mankianer.gutenachtbot.telegram.commands.admin;

import de.mankianer.gutenachtbot.telegram.CommandInterface;
import de.mankianer.gutenachtbot.telegram.TelegramUserRepo;
import de.mankianer.gutenachtbot.telegram.components.TelegramAdminComponent;
import de.mankianer.gutenachtbot.telegram.models.TelegramUser;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class AdminAprove implements CommandInterface {

    public static final String COMMAND_NAME = "/approve";
    private final TelegramAdminComponent telegramAdminComponent;
    private final TelegramUserRepo telegramUserRepo;

    public AdminAprove(TelegramAdminComponent telegramAdminComponent, TelegramUserRepo telegramUserRepo) {
        this.telegramAdminComponent = telegramAdminComponent;
        this.telegramUserRepo = telegramUserRepo;
    }

    @PostConstruct
    public void init() {
        telegramAdminComponent.registerCommand(this);
    }

    @Override
    public boolean matchesMessage(String message) {
        return message.startsWith(COMMAND_NAME + " ");
    }

    @Override
    public void onExecute(Update update, TelegramUser user) {
        String arg = update.getMessage().getText().substring(COMMAND_NAME.length() + 1);
        long userId = Long.parseLong(arg);
        telegramUserRepo.findById(userId).ifPresent(telegramAdminComponent::approveUser);
    }
}
