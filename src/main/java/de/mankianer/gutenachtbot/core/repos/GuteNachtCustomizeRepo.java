package de.mankianer.gutenachtbot.core.repos;

import de.mankianer.gutenachtbot.core.models.GuteNachtCustomize;
import de.mankianer.gutenachtbot.telegram.models.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GuteNachtCustomizeRepo extends JpaRepository<GuteNachtCustomize, String> {

    boolean existsByNameAndAuthorNot(String name, TelegramUser author);

    @Query("SELECT CASE WHEN COUNT(g) > 0 THEN TRUE ELSE FALSE END FROM GuteNachtConfig g WHERE g.customize.name = :customizeName")
    boolean existsUsersConfigsByCustomizeName(@Param("customizeName") String customizeName);

}
