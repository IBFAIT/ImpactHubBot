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
    private String botToken;

    private AuthorisationService authorisationService;

    public Concierge(String botToken) {
        super(ApiContext.getInstance(DefaultBotOptions.class));

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

    public void setAuthorisationService(AuthorisationService authorisationService) {
        this.authorisationService = authorisationService;
    }

    public String getBotUsername() {
        return "ImpactHubConciergeBot";
    }

    public String getBotToken() {
        return botToken;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        log(update);
        Message message = update.getMessage();
        processTextMessage(message);
        //todo: should be refactored to AuthenticationCommand or somewhere else
        processContactMessage(message);
    }

    private void log(Update update) {
        System.out.println("************************************************************");
        System.out.println("update = " + update);
        System.out.println("update.getUpdateId() = " + update.getUpdateId());
        System.out.println("update.getChannelPost() = " + update.getChannelPost());
        System.out.println("update.getCallbackQuery() = " + update.getCallbackQuery());
        System.out.println("update.getMessage() = " + update.getMessage());
        System.out.println("update.getChosenInlineQuery() = " + update.getChosenInlineQuery());

        if (update.hasMessage()) {
            Message message = update.getMessage();
            System.out.println("message = " + message);
            System.out.println("message.getFrom() = " + message.getFrom());
            System.out.println("message.getChat() = " + message.getChat());
            System.out.println("message.getText() = " + message.getText());
            System.out.println("message.getAuthorSignature() = " + message.getAuthorSignature());
            System.out.println("message.getCaption() = " + message.getCaption());
            System.out.println("message.getConnectedWebsite() = " + message.getConnectedWebsite());
            System.out.println("message.getChatId() = " + message.getChatId());
            System.out.println("message.getDocument() = " + message.getDocument());
            System.out.println("message.getNewChatMembers() = " + message.getNewChatMembers());
            System.out.println("message.getLeftChatMember() = " + message.getLeftChatMember());
            System.out.println("message.isUserMessage() = " + message.isUserMessage());
        }

        System.out.println("************************************************************");
    }

    private void processContactMessage(Message message) {
        if (!message.hasContact()) return;

        Contact contact = message.getContact();
        String messageText = "Thank you. Your phone number is " + contact.getPhoneNumber();

        try {
            boolean authorised = authorisationService.isAuthorised(contact.getPhoneNumber());
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