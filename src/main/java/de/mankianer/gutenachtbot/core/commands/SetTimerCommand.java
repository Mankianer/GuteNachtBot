package de.mankianer.gutenachtbot.core.commands;

import de.mankianer.gutenachtbot.core.GuteNachtConfigRepo;
import de.mankianer.gutenachtbot.core.GuteNachtService;
import de.mankianer.gutenachtbot.telegram.CommandInterface;
import de.mankianer.gutenachtbot.telegram.TelegramService;
import de.mankianer.gutenachtbot.telegram.models.TelegramUser;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class SetTimerCommand implements CommandInterface {

    private final GuteNachtService guteNachtService;
    private final GuteNachtConfigRepo guteNachtConfigRepo;

    public SetTimerCommand(GuteNachtService guteNachtService, GuteNachtConfigRepo guteNachtConfigRepo) {
        this.guteNachtService = guteNachtService;
        this.guteNachtConfigRepo = guteNachtConfigRepo;
    }

    @Override
    public boolean matchesMessage(String message, TelegramUser user) {
        return message.toLowerCase().startsWith("/timer");
    }

    @Override
    public void onExecute(Update update, TelegramUser user) {
        String text = update.getMessage().getText();
        LocalTime time = tryConvertToLocalDateTime(text);
        guteNachtService.setGuteNachtTime(user, time);
    }

    public static LocalTime tryConvertToLocalDateTime(String timeString) {
        try {
            // Extrahiere die Zeit aus dem String, z.B. "22:00"
            String time = timeString.split(" ")[1];

            // Konvertiere die extrahierte Zeit in ein LocalTime
           return LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (Exception e) {
            return null;
        }
    }
}