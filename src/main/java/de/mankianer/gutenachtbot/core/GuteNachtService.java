package de.mankianer.gutenachtbot.core;

import de.mankianer.gutenachtbot.core.model.GuteNachtConfig;
import de.mankianer.gutenachtbot.telegram.TelegramService;
import de.mankianer.gutenachtbot.telegram.model.TelegramUser;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
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
        this.timerComponent.setGuteNachtService(this);
    }

    @PostConstruct
    public void init() {

    }

    /**
     * Sends a Gute Nacht message to the user
     * Updates the nextDate in the GuteNachtConfig
     * GuteNachtConfig must be rescheduled after this method for User
     * @param user
     * @return the updated GuteNachtConfig for next Date
     */
    public GuteNachtConfig sendGuteNacht(TelegramUser user) {
        telegramService.sendMessage("Gute Nacht %s 💤".formatted(user.getFirstname()), user); //TODO hier den magischen Gutenachtgruß einfügen
        GuteNachtConfig guteNachtConfig = getGuteNachtConfig(user);
        guteNachtConfig.setNextDate(LocalDate.now().plusDays(1));
        return guteNachtConfigRepo.save(guteNachtConfig);
    }

    /**
     * Reschedules the Gute Nacht message for the user
     * used to update the timercomponent
     * @param user
     */
    public void rescheduleGuteNacht(TelegramUser user) {
        timerComponent.scheduleGuteNacht(user);
    }

    /**
     * Sets the Gute Nacht time for the user and reschedules the Gute Nacht message
     * and sends a message with the next scheduled Gute Nacht message
     * @param user
     * @param time
     */
    public void setGuteNachtTime(TelegramUser user, LocalTime time) {
        GuteNachtConfig guteNachtConfig = getGuteNachtConfig(user);
        guteNachtConfig.setTimer(time);
        guteNachtConfigRepo.save(guteNachtConfig);
        Instant nextInstant = timerComponent.scheduleGuteNacht(user);
        if(nextInstant != null) {
            telegramService.sendMessage("Nächster Gute Nacht Gruß ist am %s".formatted(formatInstant(nextInstant)), guteNachtConfig.getTelegramUser());
        } else {
            telegramService.sendMessage("Gute Nacht Gruß ist deaktiviert", guteNachtConfig.getTelegramUser());
        }
    }

    private static String formatInstant(Instant instant) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                .withZone(ZoneId.systemDefault());
        return formatter.format(instant);
    }

    /**
     *
     * @param user
     * @return the GuteNachtConfig for the user, if it does not exist, a new one is created and seved
     */
    public GuteNachtConfig getGuteNachtConfig(TelegramUser user) {
        return guteNachtConfigRepo.findByTelegramUser(user).orElseGet(() -> {
            GuteNachtConfig guteNachtConfig = new GuteNachtConfig(user);
            return guteNachtConfigRepo.save(guteNachtConfig);
        });
    }


}
