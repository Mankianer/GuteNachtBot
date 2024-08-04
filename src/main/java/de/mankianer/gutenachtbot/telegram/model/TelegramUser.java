package de.mankianer.gutenachtbot.telegram.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.lang.NonNull;

@Data
@Entity
public class TelegramUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String username;
    private String firstname;
    @NonNull
    private State state = State.UNVERIFIED;
    private Long chatId;

    public boolean isAdmin() {
        return state == State.ADMIN;
    }

    public boolean isUnverified() {
        return state == State.UNVERIFIED;
    }

    public enum State {
        UNVERIFIED, VERIFIED, ADMIN
    }
}
