package com.impacthub.bot.bots;

import com.impacthub.bot.bots.commands.HelpCommand;
import com.impacthub.bot.bots.commands.StartCommand;
import com.impacthub.bot.bots.commands.util.DefaultAction;
import com.impacthub.bot.services.Constants;
import com.impacthub.bot.services.Messages;
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

import static com.impacthub.bot.services.Constants.DEFAULT_MEMBERSHIP;

/**
 * This bot manages User authentication and membership purchase options
 */

public class Concierge extends TelegramLongPollingCommandBot {

    private static final Logger log = LoggerFactory.getLogger(Concierge.class);

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
                    kickChatMember(message.getChatId(), message.getFrom().getId().toString());
                } else {
                    boolean authorised = authorisationService.isAuthorised(contact);
                    if (!authorised) {
                        kickChatMember(message.getChatId(), message.getFrom().getId().toString());
                    } else {
                        String membership = authorisationService.getMembership(contact);
                        if (membership.equals(DEFAULT_MEMBERSHIP)) {
                            kickChatMember(message.getChatId(), message.getFrom().getId().toString());
                        }
                    }
                }
            } catch (ServiceException e) {
                log.error("Error occurred while validating newly joined user {}", update.getMessage().getFrom().getFirstName(), e);
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
        String messageText = Messages.CONTACT_RECEIVED_MSG + contact.getPhoneNumber();

        try {
            boolean authorised = authorisationService.isAuthorised(contact.getPhoneNumber());
            if (authorised) {
                messageText += Messages.AUTHORISED_MSG;
            } else {
                messageText += Messages.UNAUTHORISED_MSG;
            }
        } catch (ServiceException e) {
            log.error("Error while authorising by contact number : {}. Notification sent to User", contact.getPhoneNumber(), e);
            messageText = Constants.ERROR_MESSAGE;
        }

        SendMessage contactMessage = new SendMessage(
                message.getChatId(),
                messageText);
        try {
            execute(contactMessage);
            log.info("Sent message : '{}' to Phone : {}", messageText, contact.getPhoneNumber());
        } catch (TelegramApiException e) {
            log.error("Error while sending response after receiving contact number : {}", contact.getPhoneNumber(), e);
        }
    }


    /**
     * Process Text Message
     *
     * @param message User's Message object
     */
    private void processTextMessage(Message message) {
        log.debug("message.getChatId() = " + message.getChatId());

        log.debug("message.getChat().getInviteLink() = " + message.getChat().getInviteLink());

        log.debug("message.getFrom().getId() = " + message.getFrom().getId());
        log.debug("message.getFrom().getCanJoinGroups() = " + message.getFrom().getCanJoinGroups());
        log.debug("message.getChat().isChannelChat() = " + message.getChat().isChannelChat());
        log.debug("message.getChat().isGroupChat() = " + message.getChat().isGroupChat());
        log.debug("message.getChat().isSuperGroupChat() = " + message.getChat().isSuperGroupChat());

        log.debug("message.getChat().isUserChat() = " + message.getChat().isUserChat());

//        UnbanChatMember ucm = new UnbanChatMember("-1001243160013", 1063373308);
//        try {
//            execute(ucm);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }

        if (!message.hasText()) return;

        SendMessage echoMessage = new SendMessage();
        echoMessage.setChatId(message.getChatId());
        echoMessage.setText(Messages.ECHO_MSG + "\n" + message.getText());

        try {
            execute(echoMessage);
            log.debug("Sent message : '{}' to Chat ID : {}", echoMessage.getText(), message.getChatId());
        } catch (TelegramApiException e) {
            log.error("Error while processing text message", e);
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

     //   try {
        //    execute(kickChatMember);
    //    } catch (TelegramApiException e) {
       //     LOGGER.error("Error occurred while banning unauthorised User {}", userId, e);
     //   }

        log.info("Unauthorized User {} banned from joining", userId);
    }

}