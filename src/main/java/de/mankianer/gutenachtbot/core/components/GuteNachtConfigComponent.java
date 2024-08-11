package de.mankianer.gutenachtbot.core.components;

import de.mankianer.gutenachtbot.core.models.GuteNachtConfig;
import de.mankianer.gutenachtbot.core.models.GuteNachtCustomize;
import de.mankianer.gutenachtbot.core.repos.GuteNachtConfigRepo;
import de.mankianer.gutenachtbot.telegram.models.TelegramUser;
import org.springframework.stereotype.Component;

@Component
public class GuteNachtConfigComponent {
    private final GuteNachtConfigRepo guteNachtConfigRepo;

    public GuteNachtConfigComponent(GuteNachtConfigRepo guteNachtConfigRepo) {
        this.guteNachtConfigRepo = guteNachtConfigRepo;
    }

    public void updateGuteNachtCustomize(GuteNachtCustomize customize, TelegramUser user) {
        GuteNachtConfig guteNachtConfig = getGuteNachtConfig(user);
        guteNachtConfig.setCustomize(customize);
        guteNachtConfigRepo.save(guteNachtConfig);
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

    public GuteNachtConfig updateGuteNachtConfigToNextDay(TelegramUser user) {
        GuteNachtConfig guteNachtConfig = getGuteNachtConfig(user);
        guteNachtConfig.setNextDate(guteNachtConfig.getNextDate().plusDays(1));
        return guteNachtConfigRepo.save(guteNachtConfig);
    }

    public void saveGuteNachtConfig(GuteNachtConfig config) {
        guteNachtConfigRepo.save(config);
    }
}
