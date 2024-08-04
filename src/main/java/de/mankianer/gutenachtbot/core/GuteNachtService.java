package de.mankianer.gutenachtbot.core;

import de.mankianer.gutenachtbot.core.model.GuteNachtConfig;
import de.mankianer.gutenachtbot.telegram.TelegramService;
import de.mankianer.gutenachtbot.telegram.model.TelegramUser;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class GuteNachtService {

    private final TelegramService telegramService;
    private final GuteNachtConfigRepo guteNachtConfigRepo;
    private final TimerComponent timerComponent;


    public GuteNachtService(TelegramService telegramService, GuteNachtConfigRepo guteNachtConfigRepo, TimerComponent timerComponent) {
        this.telegramService = telegramService;
        this.guteNachtConfigRepo = guteNachtConfigRepo;
        this.timerComponent = timerComponent;
    }

    @PostConstruct
    public void init() {
        guteNachtConfigRepo.findAll().forEach(guteNachtConfig -> {
            LocalTime timer = guteNachtConfig.getTimer();
            if(timer != null) {
                timerComponent.scheduleGuteNacht(guteNachtConfig, () -> sendGuteNacht(guteNachtConfig));
            }
        });
    }

    public void sendGuteNacht(GuteNachtConfig guteNachtConfig) {
        TelegramUser user = guteNachtConfig.getTelegramUser();
        telegramService.sendMessage("Gute Nacht %s 💤".formatted(user.getFirstname()), user);

        guteNachtConfig.setNextDate(guteNachtConfig.getNextDate().plusDays(1));
        scheduleGuteNacht(guteNachtConfig);
    }

    public void scheduleGuteNacht(GuteNachtConfig guteNachtConfig) {
        Instant instant = timerComponent.scheduleGuteNacht(guteNachtConfig, () -> sendGuteNacht(guteNachtConfig));
        if(instant != null) {
            telegramService.sendMessage("Nächster Gute Nacht Gruß ist am %s".formatted(formatInstant(instant)), guteNachtConfig.getTelegramUser());
        } else {
            telegramService.sendMessage("Gute Nacht Gruß ist deaktiviert", guteNachtConfig.getTelegramUser());
        }
    }

    private static String formatInstant(Instant instant) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                .withZone(ZoneId.systemDefault());
        return formatter.format(instant);
    }


}
