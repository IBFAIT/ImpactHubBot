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
 * Help command for the User. It displays a list of commands offered by the ImpactHub Bot.
 */
public class HelpCommand extends BotCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelpCommand.class);
    private final ICommandRegistry commandRegistry;

    /**
     * Add /help command to command registry.
     */
    public HelpCommand(ICommandRegistry commandRegistry) {
        super("help", "Get all the commands this bot provides");
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

        StringBuilder helpMessageBuilder = new StringBuilder("<b>Help</b>\n");
        helpMessageBuilder.append(Messages.HELP_MSG+"\n\n");

        for (IBotCommand botCommand : commandRegistry.getRegisteredCommands()) {
            helpMessageBuilder.append(botCommand.toString()).append("\n\n");
        }

        SendMessage helpMessage = new SendMessage();
        helpMessage.setChatId(chat.getId().toString());
        helpMessage.enableHtml(true);
        helpMessage.setText(helpMessageBuilder.toString());

        try {
            absSender.execute(helpMessage);
        } catch (TelegramApiException e) {
            LOGGER.error("Error while executing Help command.", e);
        }
    }
}