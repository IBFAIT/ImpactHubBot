package com.impacthub.server;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class GatewayBot extends TelegramLongPollingBot {

    public static final String joinMainGroup = "Join our main Telegram Group.";
    public static final String joinSupportGroup = "Join our support Telegram Group.";
    public static final String buyMembership = "Buy an ImpactHub membership.";
    public static final String start = "/start";

    public void onUpdateReceived(Update update) {
        String textContent = update.getMessage().getText();

        try {
            switch (textContent) {
                case start:
                    greet(update);
                    break;

                case joinMainGroup:
                    joinMainGroup(update);
                    break;

                case joinSupportGroup:
                    joinSupportGroup(update);
                    break;

                case buyMembership:
                    buyMembership(update);
                    break;

                default:
                    break;

            }
        }catch (TelegramApiException | IOException | GeneralSecurityException e){
            e.printStackTrace();
        }
    }

    public String getBotUsername() {
        return "IH_Gateway_bot";
    }

    public String getBotToken() {
        return "1145375112:AAEEkBw1p269B6_xqUJjJVu4XiA65T8PTcw";
    }

    public void sendMessage(Update update) throws TelegramApiException {
            }

    public void greet(Update update) throws TelegramApiException{

        if(update.hasMessage() && update.getMessage().hasText()){
            SendMessage message = new SendMessage();
            message.setText("Welcome! I’m ImpactHub’s Gateway Bot. How can I help?");
            setButtons(message);
            message.setChatId(update.getMessage().getChatId());
            execute(message);
        }
    }

    public synchronized void setButtons(SendMessage sendMessage) {

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardButton keyboardButton1 = new KeyboardButton();
        keyboardButton1.setText("Join our main Telegram Group.");
        keyboardFirstRow.add(keyboardButton1);

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        KeyboardButton keyboardButton2 = new KeyboardButton();
        keyboardButton2.setText("Join our support Telegram Group.");
        keyboardSecondRow.add(keyboardButton2);

        KeyboardRow keyboardThirdRow = new KeyboardRow();
        KeyboardButton keyboardButton3 = new KeyboardButton();
        keyboardButton3.setText("Buy an ImpactHub membership.");
        keyboardThirdRow.add(keyboardButton3);

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardThirdRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public void joinMainGroup(Update update) throws TelegramApiException, IOException, GeneralSecurityException {
        if (AccessSheets.validateByTelegramId(update.getMessage().getFrom().getId()) != null) {
            execute(new SendMessage()
                    .setChatId(update.getMessage().getChatId())
                    .setText("Authorization Successful.\n" +
                            "Click on the link below to join the chat group\n" +
                            "https://t.me/joinchat/M9ncJR0-TljkE4Hm_dnmfw"));
        }
        else{
            execute(new SendMessage()
                    .setChatId(update.getMessage().getChatId())
                    .setText("Unauthorized"));
        }
    }

    public void joinSupportGroup(Update update) throws TelegramApiException {
        execute(new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText("Processing support chat joining request"));
    }

    public void buyMembership(Update update) throws TelegramApiException {
        execute(new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText("Processing purchase membership request"));
    }
}
