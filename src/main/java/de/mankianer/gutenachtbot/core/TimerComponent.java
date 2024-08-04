package de.mankianer.gutenachtbot.core;

import de.mankianer.gutenachtbot.core.model.GuteNachtConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Component
public class TimerComponent {

    private TaskScheduler taskScheduler;
    private Map<Long, ScheduledFuture<?>> scheduledTaskMap;

    @PostConstruct
    public void init() {
        scheduledTaskMap = new HashMap<>();
        taskScheduler = new ThreadPoolTaskScheduler();
        ((ThreadPoolTaskScheduler) taskScheduler).initialize();
    }

    public Instant scheduleGuteNacht(GuteNachtConfig guteNachtConfig, Runnable task) {
        if (scheduledTaskMap.containsKey(guteNachtConfig.getId())) {
            scheduledTaskMap.get(guteNachtConfig.getId()).cancel(false);
        }
        if(guteNachtConfig.getTimer() != null) {
            Instant nextInstant = getNextInstant(guteNachtConfig.getTimer().atDate(guteNachtConfig.getNextDate()));
            scheduledTaskMap.put(guteNachtConfig.getId(),taskScheduler.schedule(task, nextInstant));
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
