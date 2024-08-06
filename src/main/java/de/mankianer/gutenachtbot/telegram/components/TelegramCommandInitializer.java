package de.mankianer.gutenachtbot.telegram.components;

import de.mankianer.gutenachtbot.telegram.CommandInterface;
import de.mankianer.gutenachtbot.telegram.TelegramService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TelegramCommandInitializer  implements InitializingBean {

    private TelegramService telegramService;
    private List<CommandInterface> commands;

    public TelegramCommandInitializer(
            TelegramService telegramService,  ObjectProvider<List<CommandInterface>> commands) {
        this.telegramService = telegramService;
        this.commands = commands.getIfAvailable();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        commands.forEach(telegramService::registerCommand);
    }
}
