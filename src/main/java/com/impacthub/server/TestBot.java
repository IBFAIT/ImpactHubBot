package com.impacthub.server;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TestBot extends TelegramLongPollingBot {

    public void onUpdateReceived(Update update) {

        System.out.println("Message received:- "+update.getMessage().getText());
        String command = update.getMessage().getText();

        // Listening to user-defined command(myname)
        if(command.equals("/myname")){

            System.out.println("Name : "+update.getMessage().getFrom().getFirstName()+" "
                    +update.getMessage().getFrom().getLastName());
        }

        // Sending message to Bot
        try {
            sendMessage(update);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String getBotUsername() {
        return "thinkitive_TestBot";
    }

    public String getBotToken() {
        return "1057939283:AAGbOkTBBnN4iC3m50OFGjcOcTnyrACOLNI";
    }

    public void sendMessage(Update update) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setText("Text From Application");
        message.setChatId(update.getMessage().getChatId());
        execute(message);
    }
}
