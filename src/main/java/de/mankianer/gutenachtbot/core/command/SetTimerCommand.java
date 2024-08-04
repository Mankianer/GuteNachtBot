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

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class SetTimerCommand implements CommandInterface {

    private final TelegramService telegramService;
    private final GuteNachtService guteNachtService;
    private final GuteNachtConfigRepo guteNachtConfigRepo;

    public SetTimerCommand(TelegramService telegramService, GuteNachtService guteNachtService, GuteNachtConfigRepo guteNachtConfigRepo) {
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
        return message.toLowerCase().startsWith("/timer");
    }

    @Override
    public void onExecute(Update update, TelegramUser user) {
        GuteNachtConfig guteNachtConfig = guteNachtConfigRepo.findByTelegramUser(user).orElse(new GuteNachtConfig(user));
        String text = update.getMessage().getText();
        if(text.length() > 7 && text.split(" ").length > 1) {
            LocalTime time = convertToLocalDateTime(text);
            guteNachtConfig.setTimer(time);
        } else {
            guteNachtConfig.setTimer(null);
        }
        guteNachtService.scheduleGuteNacht(guteNachtConfig);
    }

    public static LocalTime convertToLocalDateTime(String timeString) {
        // Extrahiere die Zeit aus dem String, z.B. "22:00"
        String time = timeString.split(" ")[1];

        // Konvertiere die extrahierte Zeit in ein LocalTime
       return LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
    }
}
