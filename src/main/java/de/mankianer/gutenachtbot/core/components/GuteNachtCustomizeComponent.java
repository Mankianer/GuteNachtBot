package de.mankianer.gutenachtbot.core.components;

import de.mankianer.gutenachtbot.core.components.exceptions.UserNotAllowedException;
import de.mankianer.gutenachtbot.core.models.GuteNachtCustomize;
import de.mankianer.gutenachtbot.core.repos.GuteNachtCustomizeRepo;
import de.mankianer.gutenachtbot.telegram.models.TelegramUser;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Log4j2
@Component
public class GuteNachtCustomizeComponent {

    private final GuteNachtCustomizeRepo guteNachtCustomizeRepo;
    private final Map<String, String> defaultCustomizes;

    public GuteNachtCustomizeComponent(GuteNachtCustomizeRepo guteNachtCustomizeRepo, @Value("#{${gutenachtbot.default.customizes}}") Map<String, String> defaultCustomizes) {
        this.guteNachtCustomizeRepo = guteNachtCustomizeRepo;
        this.defaultCustomizes = defaultCustomizes;
    }

    @PostConstruct
    public void init() {
        createDefault();
    }

    public void saveCustomize(String name, String value, TelegramUser user) throws UserNotAllowedException {
        if(guteNachtCustomizeRepo.existsByNameAndAuthorNot(name, user)) {
            throw new UserNotAllowedException("Du bist nicht der Author dieses Customizes!");
        }
        guteNachtCustomizeRepo.save(new GuteNachtCustomize(name, value, user));
    }

    private void createDefault() {
        log.debug("Creating default customizes");
        defaultCustomizes.forEach((key, value) -> {
            guteNachtCustomizeRepo.findById(key).ifPresentOrElse(
                    guteNachtCustomize -> {
                    },
                    () -> guteNachtCustomizeRepo.save(new GuteNachtCustomize(key, value)));
        });
    }
}
