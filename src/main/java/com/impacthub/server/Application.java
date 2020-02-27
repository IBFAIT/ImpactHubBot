package com.impacthub.server;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class Application {

    public static void main(String[] args) {

        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new TestBot());
            telegramBotsApi.registerBot(new GatewayBot());

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        // Uncomment to work with Sheets
        /*try {
            AccessSheets.connect();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }*/
    }
}
