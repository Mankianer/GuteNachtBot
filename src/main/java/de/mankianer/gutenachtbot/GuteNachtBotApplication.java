package de.mankianer.gutenachtbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories
@EnableScheduling
public class GuteNachtBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuteNachtBotApplication.class, args);
    }

}
