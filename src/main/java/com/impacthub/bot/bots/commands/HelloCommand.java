package com.impacthub.bot.bots.commands;

import com.impacthub.bot.services.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * This command simply replies with a hello to the users command and
 * sends them the 'kind' words back, which they send via command parameters.
 */
public class HelloCommand extends BotCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloCommand.class);

    /**
     * Add /hello to command registry.
     */
    public HelloCommand() {
        super("hello", "Say hallo to this bot");
    }

    /**
     * Execute the command
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

        StringBuilder messageTextBuilder = new StringBuilder("Hello ").append(userName);
        if (arguments != null && arguments.length > 0) {
            messageTextBuilder.append("\n");
            messageTextBuilder.append(Messages.ARGS_RECEIVED_MSG);
            messageTextBuilder.append(String.join(" ", arguments));
        }

        SendMessage answer = new SendMessage();
        answer.setChatId(chat.getId().toString());
        answer.setText(messageTextBuilder.toString());

        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            LOGGER.error("Error while executing Hello command.", e);
        }
    }
}
