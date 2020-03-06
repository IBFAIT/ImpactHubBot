package com.impacthub.bot.bots;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

/**
 * Bots requests for User's phone number and location.
 */

public class TestBot extends TelegramLongPollingBot {

    private String botToken;
    private String botUsername;

    /**
     * Sets Bot Authentication Token
     *
     * @param botToken Authentication Token
     */
    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }


    /**
     * Sets Bot UserName
     *
     * @param botUsername Bot Username
     */
    public void setBotUsername(String botUsername) {
        this.botUsername = botUsername;
    }


    /**
     * This method is called when receiving updates via GetUpdates method.
     *
     * @param update Update received
     */
    public void onUpdateReceived(Update update) {

        if (update.getMessage().getContact() != null) {
            Contact contact = update.getMessage().getContact();
            try {
                getLocation(update);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        if (update.getMessage().getLocation() != null) {
            Location location = update.getMessage().getLocation();

            try {
                execute(new SendMessage()
                        .setChatId(update.getMessage().getChatId())
                        .setText("Thank you for sharing your details"));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        try {
            if (update.getMessage().getText().equals("/start")) {
                greet(update);
            }
            if (update.getMessage().getText().equals("Yes")) {
                getContact(update);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns Bot Username
     *
     * @return botUsername Bot username
     */
    public String getBotUsername() {
        return botUsername;
    }


    /**
     * Retuns Bot Authentication Token
     *
     * @return botToken Authentication token
     */
    public String getBotToken() {
        return botToken;
    }


    public void sendMessage(Update update) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());
        message.setText("");
        execute(message);
    }

    /**
     * Welcome message for the User
     *
     * @param update Update received
     */
    public void greet(Update update) throws TelegramApiException {

        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage();
            StringBuilder msgBuilder = new StringBuilder();
            msgBuilder.append("Greetings ").append(update.getMessage().getFrom().getFirstName()).append(" !");
            msgBuilder.append("\n Please connect with us by sharing your contact number.");
            message.setText(String.valueOf(msgBuilder));
            setButtons(message);
            message.setChatId(update.getMessage().getChatId());
            execute(message);
        }
    }


    /**
     * Set markup for Message
     *
     * @param sendMessage SendMessage instance
     */
    public synchronized void setButtons(SendMessage sendMessage) {

        // Create a keyboard
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        // Create a list of keyboard rows
        List<KeyboardRow> keyboard = new ArrayList<>();

        // First keyboard row
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        // Add buttons to the first keyboard row
        keyboardFirstRow.add(new KeyboardButton("Yes"));

        // Second keyboard row
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        // Add the buttons to the second keyboard row
        keyboardSecondRow.add(new KeyboardButton("No"));

        // Add all of the keyboard rows to the list
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        // and assign this list to our keyboard
        replyKeyboardMarkup.setKeyboard(keyboard);
    }


    /**
     * Request user for contact
     *
     * @param update Update received
     */
    public void getContact(Update update) {

        if (update.getMessage().getText().equals("Yes")) {
            long chat_id = update.getMessage().getChatId();

            SendMessage sendMessage = new SendMessage()
                    .setChatId(chat_id)
                    .setText("Waiting for contact....");

            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
            replyKeyboardMarkup.setSelective(true);
            replyKeyboardMarkup.setResizeKeyboard(true);
            replyKeyboardMarkup.setOneTimeKeyboard(true);

            List<KeyboardRow> keyboard = new ArrayList<>();

            KeyboardRow keyboardFirstRow = new KeyboardRow();
            KeyboardButton keyboardButton = new KeyboardButton();
            keyboardButton.setText("Click to share your contact number !").setRequestContact(true);
            keyboardFirstRow.add(keyboardButton);

            keyboard.add(keyboardFirstRow);

            replyKeyboardMarkup.setKeyboard(keyboard);

            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Request user for location
     *
     * @param update Update received
     */
    public void getLocation(Update update) throws TelegramApiException {

        execute(new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText("Thank you, kindly share your location as well. "));

        long chat_id = update.getMessage().getChatId();
        SendMessage sendMessage = new SendMessage()
                .setChatId(chat_id)
                .setText("Waiting for location....");

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton();
        keyboardButton.setText("Click to share your location !").setRequestLocation(true);
        keyboardFirstRow.add(keyboardButton);

        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}