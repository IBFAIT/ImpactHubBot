package com.impacthub.bot.bots.commands;

import com.impacthub.bot.services.Messages;
import com.impacthub.bot.services.authorisation.AuthorisationService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class JoinCommand extends BotCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(JoinCommand.class);
    private final AuthorisationService authService;

    String jobAndOpportunitiesGroupUrl = "https://t.me/ImpactHubJobsOpportunities";
    String spaceAlertsGroupUrl = "https://t.me/ImpactHubSpaceAlerts";
    String dorfplatzGroupUrl = "https://t.me/ImpactHubDorfplatz";


    /**
     * Instantiate {@link JoinCommand} command with {@link AuthorisationService}.
     * Add /join to command registry.
     */
    public JoinCommand(AuthorisationService authService) {
        super("join", "Join Telegram Groups");
        this.authService = authService;
    }


    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        String phoneNumber = authService.getPhoneNumberFromUserId(user.getId());

        SendMessage answer = new SendMessage();
        StringBuilder messageTextBuilder = new StringBuilder();

        // TODO with optional
        if (phoneNumber == null) {
            messageTextBuilder.append(Messages.ILLEGAL_ACTION);
            answer.setText(messageTextBuilder.toString());
        } else {
            InlineKeyboardMarkup inlineKeyboardMarkup = getInlineKeyboardMarkup();
            answer.setReplyMarkup(inlineKeyboardMarkup);
            answer.setText(Messages.GROUP_JOIN_OPTIONS_MSG);
        }

        answer.setChatId(chat.getId().toString());

        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            LOGGER.error("Error while executing Join command.", e);
        }
    }


    //todo: don't display the group I'm in when executing /join
    @NotNull
    private InlineKeyboardMarkup getInlineKeyboardMarkup() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        InlineKeyboardButton rowInlineButton = new InlineKeyboardButton();
        rowInlineButton.setText("Join ImpactHub Job & Opportunities Group.");
        rowInlineButton.setUrl(jobAndOpportunitiesGroupUrl);
        rowInline.add(rowInlineButton);

        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        InlineKeyboardButton rowInline2Button = new InlineKeyboardButton();
        rowInline2Button.setText("Join Space Alerts Group.");
        rowInline2Button.setUrl(spaceAlertsGroupUrl);
        rowInline2.add(rowInline2Button);

        List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
        InlineKeyboardButton rowInline3Button = new InlineKeyboardButton();
        rowInline3Button.setText("Join Dorfplatz");
        rowInline3Button.setUrl(dorfplatzGroupUrl);
        rowInline3.add(rowInline3Button);

        rowsInline.add(rowInline);
        rowsInline.add(rowInline2);
        rowsInline.add(rowInline3);

        inlineKeyboardMarkup.setKeyboard(rowsInline);

        return inlineKeyboardMarkup;

    }
}
