package de.mankianer.gutenachtbot.core;

import de.mankianer.gutenachtbot.core.models.GuteNachtConfig;
import de.mankianer.gutenachtbot.telegram.models.TelegramUser;
import jakarta.annotation.PostConstruct;
import lombok.Setter;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Component
public class TimerComponent {

    private final GuteNachtConfigRepo guteNachtConfigRepo;
    private TaskScheduler taskScheduler;
    private Map<Long, ScheduledFuture<?>> scheduledTaskMap;
    @Setter
    private GuteNachtService guteNachtService;

    public TimerComponent(GuteNachtConfigRepo guteNachtConfigRepo) {
        this.guteNachtConfigRepo = guteNachtConfigRepo;
        scheduledTaskMap = new HashMap<>();
        taskScheduler = new ThreadPoolTaskScheduler();
        ((ThreadPoolTaskScheduler) taskScheduler).initialize();
    }

    @PostConstruct
    public void init() {
        reScheduleAll();
    }

    private void reScheduleAll() {
        guteNachtConfigRepo.findAll().forEach(guteNachtConfig -> {
           scheduleGuteNacht(guteNachtConfig.getTelegramUser());
        });
    }

    /**
     * Schedules the Gute Nacht message for the user
     * @param user
     * @return the next instant the message will be sent
     */
    public Instant scheduleGuteNacht(TelegramUser user) {
        if (scheduledTaskMap.containsKey(user.getId())) {
            scheduledTaskMap.get(user.getId()).cancel(false);
        }
        GuteNachtConfig guteNachtConfig = guteNachtService.getGuteNachtConfig(user);
        if(guteNachtConfig.getTimer() != null) {
            Instant nextInstant = getNextInstant(guteNachtConfig.getTimer().atDate(guteNachtConfig.getNextDate()));
            scheduledTaskMap.put(user.getId(),taskScheduler.schedule(() -> {
                guteNachtService.sendGuteNacht(user);
                scheduleGuteNacht(user);
            }, nextInstant));
            return nextInstant;
        }
        return null;
    }

    private static Instant getNextInstant(LocalDateTime time) {
        // Falls der Zeitpunkt in der Vergangenheit liegt, einen Tag hinzufügen
        if (LocalDateTime.now().compareTo(time) > 0) {
            time = time.plusDays(1);
        }
        return time.atZone(ZoneId.systemDefault()).toInstant();
    }
}
