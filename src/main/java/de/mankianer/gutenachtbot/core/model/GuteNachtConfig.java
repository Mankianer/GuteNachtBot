package de.mankianer.gutenachtbot.core.model;

import de.mankianer.gutenachtbot.telegram.model.TelegramUser;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
public class GuteNachtConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private TelegramUser telegramUser;

    private LocalTime timer;

    private LocalDate nextDate;

    public GuteNachtConfig(TelegramUser telegramUser) {
        this.telegramUser = telegramUser;
        this.nextDate = LocalDate.now();
    }
}
