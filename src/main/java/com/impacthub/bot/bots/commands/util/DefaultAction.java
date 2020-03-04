package com.impacthub.bot.bots.commands.util;

import com.impacthub.bot.bots.commands.HelpCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.function.BiConsumer;

public class DefaultAction implements BiConsumer<AbsSender, Message> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAction.class);
    private final HelpCommand helpCommand;

    public DefaultAction(HelpCommand helpCommand) {
        this.helpCommand = helpCommand;
    }

    @Override
    public void accept(AbsSender absSender, Message message) {
        SendMessage commandUnknownMessage = new SendMessage();
        commandUnknownMessage.setChatId(message.getChatId());
        commandUnknownMessage.setText("The command '" + message.getText() + "' is not known by this bot. Here comes some help ");
        try {
            absSender.execute(commandUnknownMessage);
        } catch (TelegramApiException e) {
            LOGGER.error("Error while executing default action.", e);
        }
        helpCommand.execute(absSender, message.getFrom(), message.getChat(), new String[]{});
    }
}
