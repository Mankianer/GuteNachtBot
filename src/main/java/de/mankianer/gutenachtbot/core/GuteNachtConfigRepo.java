package de.mankianer.gutenachtbot.core;

import de.mankianer.gutenachtbot.core.models.GuteNachtConfig;
import de.mankianer.gutenachtbot.telegram.models.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuteNachtConfigRepo extends JpaRepository<GuteNachtConfig, Long> {

    Optional<GuteNachtConfig> findByTelegramUser(TelegramUser user);
}
