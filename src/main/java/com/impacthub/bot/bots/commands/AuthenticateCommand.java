package com.impacthub.bot.bots.commands;

import com.impacthub.bot.services.authorisation.AuthorisationService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for Authentication of the User.
 */
public class AuthenticateCommand extends BotCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticateCommand.class);
    private final AuthorisationService authService;

    /**
     * Instantiate {@link AuthenticateCommand} command with {@link AuthorisationService}.
     * Add /authenticate to command registry.
     * */
    public AuthenticateCommand(AuthorisationService authService) {
        super("authenticate", "Do authenticate");
        this.authService = authService;
    }


    /**
     * Authenticates User by phone number.
     * Execute the command.
     *
     * @param absSender absSender to send messages over.
     * @param user      the user who sent the command.
     * @param chat      the chat, to be able to send replies.
     * @param arguments passed arguments.
     */
    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        String userName = chat.getUserName();
        if (userName == null || userName.isEmpty()) {
            userName = user.getFirstName() + " " + user.getLastName();
        }

        String phoneNumber = authService.getPhoneNumberFromUserId(user.getId());

        SendMessage answer = new SendMessage();
        StringBuilder messageTextBuilder = new StringBuilder("Hello ").append(userName);

        //todo replace /w optional...
        if (phoneNumber != null) {
            messageTextBuilder.append("\nYou already authenticated w/ your phone number. Many thanks.");

        } else {
            if (!chat.isUserChat()) {
                messageTextBuilder.append("\nI'm sorry, you can't authenticate within a group or supergroup - That's for your own security. Please get in touch w/ me directly to authenticate properly: https://t.me/@ImpactHubConciergeBot");
            } else {
                messageTextBuilder.append("\nIn order to join our channels you have to authenticate with your phone number. ");

                ReplyKeyboardMarkup replyKeyboardMarkup = getReplyKeyboardMarkup();

                answer.setReplyMarkup(replyKeyboardMarkup);
            }
        }

        answer.setChatId(chat.getId().toString());
        answer.setText(messageTextBuilder.toString());

        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            LOGGER.error("Error while executing Hello command.", e);
        }
    }

    @NotNull
    private ReplyKeyboardMarkup getReplyKeyboardMarkup() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

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
        return replyKeyboardMarkup;
    }
}
