package com.impacthub.bot.bots.commands;

import com.impacthub.bot.Application;
import com.impacthub.bot.bots.Concierge;
import com.impacthub.bot.services.Constants;
import com.impacthub.bot.services.authorisation.AuthorisationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MembershipCommand extends BotCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(MembershipCommand.class);

    public MembershipCommand() {
        super("membership", "Get Membership Status");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        //todo: userName is never used
        String userName = chat.getUserName();
        if (userName == null || userName.isEmpty()) {
            userName = user.getFirstName();
        }

        String phoneNumber = Concierge.getPhoneNumber(user.getId());
        SendMessage message = new SendMessage();
        message.setChatId(chat.getId());

        try {
            if (phoneNumber != null) {
                AuthorisationService authService = (AuthorisationService) Application.getBean("authorisationService");
                String membership = authService.getMembership(phoneNumber);

                if (!membership.equals(Constants.DEFAULT_MEMBERSHIP)) {
                    message.setText("Your membership is [" + membership + "].");
                } else {
                    message.setText(Constants.NO_MEMBERSHIP);
                }
            } else
                message.setText(String.format(Constants.UNAUTHORIZED_USER, this.getCommandIdentifier()));

            absSender.execute(message);


        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
