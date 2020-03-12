package com.impacthub.bot.bots.commands;

import com.impacthub.bot.services.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.ICommandRegistry;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Starts the conversation with the bot.
 * Welcomes User with a welcome message and informs what ImpactHub bot offers.
 */
public class StartCommand extends BotCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartCommand.class);
    private final ICommandRegistry commandRegistry;


    /**
     * Instantiate {@link StartCommand}
     * add /start to Command Registry
     */
    public StartCommand(ICommandRegistry commandRegistry) {
        super("start", "With this command you can start the Bot");
        this.commandRegistry = commandRegistry;
    }

    /**
     * Execute the command
     *
     * @param absSender absSender to send messages over.
     * @param user      the user who sent the command.
     * @param chat      the chat, to be able to send replies.
     * @param strings   passed arguments.
     */
    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        StringBuilder messageBuilder = new StringBuilder();

        String userName = user.getFirstName() + " " + user.getLastName();

        messageBuilder.append("\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\n\n");
        messageBuilder.append("\uD83D\uDC4B ").append(Messages.WELCOME).append(userName).append("! \uD83D\uDC4B \n\n ");
        messageBuilder.append(Messages.WELCOME_MSG1).append("\n\n");
        messageBuilder.append(Messages.WELCOME_MSG2).append("\n\n");

        for (IBotCommand botCommand : commandRegistry.getRegisteredCommands()) {
            messageBuilder.append(botCommand.toString()).append("\n\n");
        }

        messageBuilder.append("\uD83E\uDD14\uD83E\uDD14\uD83E\uDD14\uD83E\uDD14\uD83E\uDD14\uD83E\uDD14\uD83E\uDD14\uD83E\uDD14\uD83E\uDD14\uD83E\uDD14\uD83E\uDD14\uD83E\uDD14\uD83E\uDD14\uD83E\uDD14\uD83E\uDD14\uD83E\uDD14\uD83E");

        SendMessage answer = new SendMessage();
        answer.setChatId(chat.getId().toString());
        answer.setText(messageBuilder.toString());
        answer.enableHtml(true);

        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            LOGGER.error("Error while executing Start command.", e);
        }
    }

}
