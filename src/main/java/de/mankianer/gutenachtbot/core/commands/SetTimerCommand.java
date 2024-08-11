package de.mankianer.gutenachtbot.core.commands;

import de.mankianer.gutenachtbot.core.GuteNachtService;
import de.mankianer.gutenachtbot.telegram.commands.SimpleCommand;
import de.mankianer.gutenachtbot.telegram.models.TelegramUser;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class SetTimerCommand extends SimpleCommand {

    private final GuteNachtService guteNachtService;

    public SetTimerCommand(GuteNachtService guteNachtService) {
        super("/timer", "Zum Setzen des GuteNachtGeschichten Timers.(/timer [<HH:mm>])\nBeispiel: /timer 22:00\nBeispiel: /timer off");
        this.guteNachtService = guteNachtService;
    }

    @Override
    public void onExecute(Update update, TelegramUser user) {
        String text = update.getMessage().getText();
        LocalTime time = tryConvertToLocalDateTime(text);
        guteNachtService.setGuteNachtTime(user, time);
    }

    private static LocalTime tryConvertToLocalDateTime(String timeString) {
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
