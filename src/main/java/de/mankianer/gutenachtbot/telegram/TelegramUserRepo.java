package de.mankianer.gutenachtbot.telegram;

import de.mankianer.gutenachtbot.telegram.models.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TelegramUserRepo extends JpaRepository<TelegramUser, Long> {
    List<TelegramUser> findByUsername(String username);
    Optional<TelegramUser> findByChatId(long chatId);
    Optional<TelegramUser> findFirstByChatIdOrUsername(long chatId, String username);
    List<TelegramUser> findByStateAndChatIdNotNull(TelegramUser.State state);
}
