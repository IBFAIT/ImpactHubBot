package com.impacthub.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

import java.util.List;

/**
 * Main execution class of Application
 */
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    /**
     * Registers bots with Telegram API
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) throws TelegramApiRequestException {

        log.info("Starting bot ...");

        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");

        List<LongPollingBot> bots = (List<LongPollingBot>) context.getBean("bots");

        for (Object obj : bots) {
            LongPollingBot bot = (LongPollingBot) obj;

            telegramBotsApi.registerBot(bot);
        }

        log.info("Bot started.");

    }
}