package de.mankianer.gutenachtbot.core.repos;

import de.mankianer.gutenachtbot.core.models.GuteNachtConfig;
import de.mankianer.gutenachtbot.core.models.GuteNachtCustomize;
import de.mankianer.gutenachtbot.telegram.models.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuteNachtCustomizeRepo extends JpaRepository<GuteNachtCustomize, String> {

    boolean existsByNameAndAuthorNot(String name, TelegramUser author);
}
