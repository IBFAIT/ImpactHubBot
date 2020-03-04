package com.impacthub.bot.bots;

import com.impacthub.bot.bots.commands.*;
import com.impacthub.bot.bots.commands.util.DefaultAction;
import com.impacthub.bot.services.ServiceException;
import com.impacthub.bot.services.authorisation.AuthorisationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

public class Concierge extends TelegramLongPollingCommandBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(Concierge.class);

    private static Map<Integer, String> userIdToPhone = new HashMap<>();

    private String botUsername;
    private String botToken;

    private AuthorisationService authService;

    public Concierge(String botUsername, String botToken) {
        super(ApiContext.getInstance(DefaultBotOptions.class));

        this.botUsername = botUsername;
        this.botToken = botToken;

        HelpCommand helpCommand = new HelpCommand(this);

        register(new HelloCommand());
        register(new StartCommand(this));
        register(helpCommand);
        register(new AuthenticateCommand());
        register(new MembershipCommand());

        registerDefaultAction(new DefaultAction(helpCommand));
    }

    public void setAuthorisationService(AuthorisationService authService) {
        this.authService = authService;
    }

    public String getBotUsername() {
        return "ImpactHubConciergeBot";
    }

    public String getBotToken() {
        return botToken;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();

            //TODO: should be refactored to AuthenticationCommand or somewhere else
            processTextMessage(message);
            processContactMessage(message);
        }
    }

    private void processContactMessage(Message message) {
        if (!message.hasContact()) return;

        Contact contact = message.getContact();
        userIdToPhone.put(contact.getUserID(), contact.getPhoneNumber());
        String messageText = "Thank you. Your phone number is " + contact.getPhoneNumber();

        try {
            boolean authorised = authService.isAuthorised(contact.getPhoneNumber());
            if (authorised) {
                messageText += ". You're authorised to join our groups.";
            } else {
                messageText += ". I'm sorry, you're not authorised to join our groups. You should buy an Impact Hub membership first.";
            }

            SendMessage contactMessage = new SendMessage(
                    message.getChatId(),
                    messageText);

            execute(contactMessage);
            LOGGER.info("Sent message : '{}' to Phone : {}", messageText, contact.getPhoneNumber());
        } catch (TelegramApiException e) {
            LOGGER.error("Error while sending response after receiving contact number : {}", contact.getPhoneNumber(), e);
        } catch (ServiceException e) {
            LOGGER.error("Error while authorising by contact number : {}", contact.getPhoneNumber(), e);
        }
    }

    private void processTextMessage(Message message) {
        if (!message.hasText()) return;

        SendMessage echoMessage = new SendMessage();
        echoMessage.setChatId(message.getChatId());
        echoMessage.setText("Hey here's your message:\n" + message.getText());

        try {
            execute(echoMessage);
            LOGGER.info("Sent message : '{}' to Chat ID : {}", echoMessage.getText(), message.getChatId());
        } catch (TelegramApiException e) {
            LOGGER.error("Error while processing text message", e);
        }
    }

    public static String getPhoneNumber(Integer userID) {
        return userIdToPhone.get(userID);
    }

}