package com.impacthub.bot.bots;

import com.impacthub.bot.bots.commands.AuthenticateCommand;
import com.impacthub.bot.bots.commands.HelloCommand;
import com.impacthub.bot.bots.commands.HelpCommand;
import com.impacthub.bot.bots.commands.StartCommand;
import com.impacthub.bot.bots.commands.util.DefaultAction;
import com.impacthub.bot.services.ServiceException;
import com.impacthub.bot.services.authorisation.AuthorisationService;
import org.telegram.bot.services.BotLogger;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Concierge extends TelegramLongPollingCommandBot {
    public static final String joinMainGroup = "Join our main Telegram Group.";
    public static final String joinSupportGroup = "Join our support Telegram Group.";
    public static final String buyMembership = "Buy an ImpactHub membership.";
    public static final String start = "/start";

    private String botToken;
    private String botUsername;

    private AuthorisationService service;

    public Concierge(String botuserName, String botToken) {
        super(ApiContext.getInstance(DefaultBotOptions.class));

        this.botUsername = botUsername;
        this.botToken = botToken;

        HelpCommand helpCommand = new HelpCommand(this);

        register(new HelloCommand());
        register(new StartCommand(this));
        register(helpCommand);
        register(new AuthenticateCommand());

        registerDefaultAction(new DefaultAction(helpCommand));
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public void setBotUsername(String botUsername) {
        this.botUsername = botUsername;
    }

    public void setAuthorisationService(AuthorisationService service) {
        this.service = service;
    }

    public String getBotUsername() {
        return "ImpactHubConciergeBot";
    }

    public String getBotToken() {
        return botToken;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        Message message = update.getMessage();
        processTextMessage(message);
        //todo: should be refactored to AuthenticationCommand or somewhere else
        processContactMessage(message);
    }

    private void processContactMessage(Message message) {
        if (!message.hasContact()) return;

        Contact contact = message.getContact();
        String messageText = "Thank you. Your phone number is " + contact.getPhoneNumber();

        try {
            boolean authorised = service.isAuthorised(contact.getPhoneNumber());
            if (authorised) {
                messageText += ". You're authorised to join our groups.";
            } else {
                messageText += ". I'm sorry, you're not authorised to join our groups. You should buy an Impact Hub membership first.";
            }

            SendMessage contactMessage = new SendMessage(
                    message.getChatId(),
                    messageText);
            execute(contactMessage);
        } catch (TelegramApiException | ServiceException e) {
            //todo refactor
            e.printStackTrace();
        }
    }

    private void processTextMessage(Message message) {
        if (!message.hasText()) return;

        SendMessage echoMessage = new SendMessage();
        echoMessage.setChatId(message.getChatId());
        echoMessage.setText("Hey here's your message:\n" + message.getText());

        try {
            execute(echoMessage);
        } catch (TelegramApiException e) {
            //todo refactor
            BotLogger.error("foo", e);
        }
    }

}
