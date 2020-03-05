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

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    /**
     * Registers bots with Telegram API
     */
    public static void main(String[] args) {

        LOGGER.info("Starting bot ...");

        try {
            ApiContextInitializer.init();
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

            ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");

            List<LongPollingBot> bots = (List<LongPollingBot>) context.getBean("bots");

            for (Object obj : bots) {
                LongPollingBot bot = (LongPollingBot) obj;
                try {
                    telegramBotsApi.registerBot(bot);
                } catch (TelegramApiRequestException e) {
                    LOGGER.error("Error while registering Bot.", e);
                }
            }

            LOGGER.info("Bot started.");
        } catch (Exception e) {
            LOGGER.error("Error while starting Bot.", e);
        }
    }
}