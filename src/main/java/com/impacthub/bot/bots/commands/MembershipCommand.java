package com.impacthub.bot.bots.commands;

import com.impacthub.bot.services.authorisation.AuthorisationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Date;

import static com.impacthub.bot.services.Constants.*;

public class MembershipCommand extends BotCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(MembershipCommand.class);
    private final AuthorisationService authService;

    public MembershipCommand(AuthorisationService authService) {
        super("membership", "Get Membership Status");
        this.authService = authService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        //todo: userName is never used
        String userName = chat.getUserName();
        if (userName == null || userName.isEmpty()) {
            userName = user.getFirstName();
        }

        String phoneNumber = authService.getPhoneNumberFromUserId(user.getId());
        SendMessage message = new SendMessage();
        message.setChatId(chat.getId());

        try {
            if (phoneNumber != null) {
                String membership = authService.getMembership(phoneNumber);

                if (!membership.equals(DEFAULT_MEMBERSHIP)) {

                    //todo!
                    Date expirationDate = authService.getMembershipExpirationDate(phoneNumber);
                    int days = 243;
                    message.setText("Your membership is '" + membership + "' and will expire in " + days + " days.");
                } else {
                    message.setText(NO_MEMBERSHIP);
                }
            } else {
                message.setText(String.format(UNAUTHORIZED_USER, this.getCommandIdentifier()));
            }

            absSender.execute(message);

        } catch (TelegramApiException e) {
            LOGGER.error("Error executing membership command", e);
            e.printStackTrace();
        }
    }
}
