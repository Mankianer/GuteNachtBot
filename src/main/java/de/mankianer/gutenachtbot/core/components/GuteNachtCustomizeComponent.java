package de.mankianer.gutenachtbot.core.components;

import de.mankianer.gutenachtbot.core.GuteNachtService;
import de.mankianer.gutenachtbot.core.components.exceptions.NotFoundException;
import de.mankianer.gutenachtbot.core.components.exceptions.UserNotAllowedException;
import de.mankianer.gutenachtbot.core.models.GuteNachtConfig;
import de.mankianer.gutenachtbot.core.models.GuteNachtCustomize;
import de.mankianer.gutenachtbot.core.repos.GuteNachtCustomizeRepo;
import de.mankianer.gutenachtbot.telegram.models.TelegramUser;
import jakarta.annotation.PostConstruct;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Log4j2
@Component
public class GuteNachtCustomizeComponent {

    private final GuteNachtCustomizeRepo guteNachtCustomizeRepo;
    private final Map<String, String> defaultCustomizes;
    @Setter
    private GuteNachtService guteNachtService;

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

    public boolean removeCustomize(String name, TelegramUser user) throws UserNotAllowedException {
        Optional<GuteNachtCustomize> customizeOp = guteNachtCustomizeRepo.findById(name);
        if(customizeOp.isEmpty()) {
            return false;
        }
        GuteNachtCustomize customize = customizeOp.get();
        if(!user.equals(customize.getAuthor())) {
            throw new UserNotAllowedException("Du bist nicht der Author dieses Customizes!");
        }
        if (guteNachtCustomizeRepo.existsUsersConfigsByCustomizeName(name)) {
            throw new UserNotAllowedException("Customize %s wird noch von User verwendet!".formatted(name));
        }
        guteNachtCustomizeRepo.deleteById(name);
        return true;
    }

    public List<GuteNachtCustomize> getAllCustomizes() {
        return guteNachtCustomizeRepo.findAll();
    }

    public GuteNachtCustomize getCurrentCustomize(TelegramUser user) {
        GuteNachtConfig guteNachtConfig = guteNachtService.getGuteNachtConfig(user);
        return guteNachtConfig.getCustomize() == null ? getDefault() : guteNachtConfig.getCustomize();
    }

    public GuteNachtCustomize setCustomize(String name, TelegramUser user) throws NotFoundException {
        GuteNachtCustomize customize = guteNachtCustomizeRepo.findById(name).orElseThrow(() -> new NotFoundException("Customize nicht gefunden"));
        guteNachtService.updateGuteNachtCustomize(customize, user);
        return customize;
    }

    public GuteNachtCustomize getDefault() {
        Map.Entry<String, String> defaultCustomize = defaultCustomizes.entrySet().iterator().next();
        return guteNachtCustomizeRepo.findById(defaultCustomize.getKey()).orElse(new GuteNachtCustomize(defaultCustomize.getKey(), defaultCustomize.getValue()));
    }

    private void createDefault() {
        log.debug("Creating default customizes");
        defaultCustomizes.forEach((key, value) -> guteNachtCustomizeRepo.findById(key).ifPresentOrElse(
                guteNachtCustomize -> {
                },
                () -> guteNachtCustomizeRepo.save(new GuteNachtCustomize(key, value))));
    }

    public Optional<GuteNachtCustomize> getCustomize(String name) {
        return guteNachtCustomizeRepo.findById(name);
    }
}
