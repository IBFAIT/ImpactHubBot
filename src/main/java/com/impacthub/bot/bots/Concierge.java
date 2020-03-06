package com.impacthub.bot.bots;

import com.impacthub.bot.bots.commands.HelpCommand;
import com.impacthub.bot.bots.commands.StartCommand;
import com.impacthub.bot.bots.commands.util.DefaultAction;
import com.impacthub.bot.services.ServiceException;
import com.impacthub.bot.services.authorisation.AuthorisationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.api.methods.groupadministration.KickChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.Duration;

/**
 * This bot manages User authentication and membership purchase options
 */

public class Concierge extends TelegramLongPollingCommandBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(Concierge.class);

    private final String botToken;

    private AuthorisationService authorisationService;

    /**
     * Register Bot Commands.
     *
     * @param botToken    Bot Token.
     * @param botCommands Array of BotCommand
     */
    public Concierge(String botToken, BotCommand... botCommands) {
        super(ApiContext.getInstance(DefaultBotOptions.class));

        this.botToken = botToken;

        for (BotCommand botCommand : botCommands) {
            register(botCommand);
        }

        register(new StartCommand(this));
        HelpCommand helpCommand = new HelpCommand(this);
        register(helpCommand);

        registerDefaultAction(new DefaultAction(helpCommand));
    }


    /**
     * Setter for {@link AuthorisationService} instance.
     */
    public void setAuthorisationService(AuthorisationService authService) {
        this.authorisationService = authService;
    }


    /**
     * Returns Bot Username
     *
     * @return Bot username
     */
    public String getBotUsername() {
        return "ImpactHubConciergeBot";
    }


    /**
     * Returns Bot Authentication Token
     *
     * @return Authentication token
     */
    public String getBotToken() {
        return botToken;
    }


    /**
     * This method is called when receiving updates via GetUpdates method.
     * If not reimplemented - it just sends updates by one into {@link #onUpdateReceived(Update)}.
     *
     * @param update Update received
     */
    @Override
    public void processNonCommandUpdate(Update update) {
        Message message = update.getMessage();

        if (!(message.getNewChatMembers().isEmpty())) {

            String contact = getPhoneNumberFromUserId(message.getFrom().getId());

            try {
                if (contact == null) {
                    kickChatMember(message.getChatId(),
                            message.getFrom().getId().toString());
                    LOGGER.info("Unauthorized User {} banned from joining", update.getMessage().getFrom().getFirstName());
                } else {
                    boolean authorised = authorisationService.isAuthorised(contact);
                    if (!authorised) {
                        kickChatMember(message.getChatId(),
                                message.getFrom().getId().toString());
                        LOGGER.info("Unauthorized User {} banned from joining", update.getMessage().getFrom().getFirstName());
                    }
                }
            } catch (ServiceException e) {
                LOGGER.error("Error occurred while validating newly joined user {}", update.getMessage().getFrom().getFirstName(), e);
            }

        }
        processTextMessage(message);

        //TODO: should be refactored to AuthenticationCommand or somewhere else
        processContactMessage(message);
    }


    /**
     * Process message with contact attached
     *
     * @param message User's Message object
     */
    private void processContactMessage(Message message) {
        if (!message.hasContact()) return;

        Contact contact = message.getContact();
        authorisationService.registerUser(contact.getUserID(), contact.getPhoneNumber());
        String messageText = "Thank you. Your phone number is " + contact.getPhoneNumber();

        try {
            boolean authorised = authorisationService.isAuthorised(contact.getPhoneNumber());
            if (authorised) {
                messageText += ". You're authorised to join our groups. Please use /join to join our groups";
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


    /**
     * Process Text Message
     *
     * @param message User's Message object
     */
    private void processTextMessage(Message message) {
        if (!message.hasText()) return;

        SendMessage echoMessage = new SendMessage();
        Long chatId = message.getChatId();
        Integer userId = message.getFrom().getId();

        echoMessage.setChatId(chatId);

        String msgText = "Hey\n Here's your message: '" + message.getText() + "'";

        msgText += "\n";
        msgText += "This chat's ID is " + chatId;
        msgText += "\n";
        msgText += "Your ID is " + userId;

        String title = message.getChat().getTitle();
        msgText += "\n";
        msgText += "The title of this chat is '" + title + "'";


        echoMessage.setText(msgText);

        try {
            execute(echoMessage);
            LOGGER.info("Sent message : '{}' to Chat ID : {}", echoMessage.getText(), chatId);
        } catch (TelegramApiException e) {
            LOGGER.error("Error while processing text message", e);
        }
    }


    /**
     * Returns User's phone number
     *
     * @param userID User's ID
     * @return String User's phone number
     */
    public String getPhoneNumberFromUserId(int userID) {
        return authorisationService.getPhoneNumberFromUserId(userID);
    }


    /**
     * Bans unauthorised users who have joined the group
     *
     * @param chatId Id of the Chat
     * @param userId Id of the User
     */
    public void kickChatMember(Long chatId, String userId) {
        KickChatMember kickChatMember = new KickChatMember();
        kickChatMember.setChatId(chatId);
        kickChatMember.setUserId(Integer.valueOf(userId));
        kickChatMember.forTimePeriod(Duration.ofMinutes(1));

        try {
            execute(kickChatMember);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}