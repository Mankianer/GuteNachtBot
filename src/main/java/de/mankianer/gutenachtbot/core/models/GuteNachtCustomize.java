package de.mankianer.gutenachtbot.core.models;

import de.mankianer.gutenachtbot.telegram.models.TelegramUser;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class GuteNachtCustomize {

    @Id
    @NonNull
    private String name;

    @NonNull
    private String prompt;

    @ManyToOne
    private TelegramUser author;

}
