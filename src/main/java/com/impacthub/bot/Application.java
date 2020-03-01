package com.impacthub.bot;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

import java.util.List;

public class Application {

    public static void main(String[] args) throws TelegramApiRequestException {

        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        List bots = (List) context.getBean("bots");

        for (Object obj : bots) {
            LongPollingBot bot = (LongPollingBot) obj;
            telegramBotsApi.registerBot(bot);
        }

    }
}
