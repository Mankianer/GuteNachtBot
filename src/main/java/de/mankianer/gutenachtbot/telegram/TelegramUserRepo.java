package de.mankianer.gutenachtbot.telegram;

import de.mankianer.gutenachtbot.telegram.model.TelegramUser;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TelegramUserRepo extends JpaRepository<TelegramUser, String> {
    List<TelegramUser> findByUsername(String username);
    Optional<TelegramUser> findByChatId(long chatId);
}
