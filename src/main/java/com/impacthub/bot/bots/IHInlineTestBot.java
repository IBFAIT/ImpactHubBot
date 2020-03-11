package com.impacthub.bot.bots;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.inlinequery.ChosenInlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

// t.me/IHInlineTestBot

public class IHInlineTestBot extends TelegramLongPollingBot {
    private static final Logger LOGGER = LoggerFactory.getLogger(IHInlineTestBot.class);

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Message channelPost = update.getChannelPost();
        ChosenInlineQuery chosenInlineQuery = update.getChosenInlineQuery();
        InlineQuery inlineQuery = update.getInlineQuery();
        Integer updateId = update.getUpdateId();

        System.out.println("message = " + message);
        System.out.println("callbackQuery = " + callbackQuery);
        System.out.println("channelPost = " + channelPost);
        System.out.println("chosenInlineQuery = " + chosenInlineQuery);
        System.out.println("inlineQuery = " + inlineQuery);
        System.out.println("updateId = " + updateId);

        User user = null;
        if (update.hasMessage()) {
            System.out.println("update.hasMessage() = " + update.hasMessage());

            user = message.getFrom();

            Integer messageId = message.getMessageId();
            System.out.println("messageId = " + messageId);

            String text = message.getText();

            System.out.println("text = " + text);

        } else if (update.hasInlineQuery()) {

            System.out.println("update.hasInlineQuery() = " + update.hasInlineQuery());

            user = inlineQuery.getFrom();

            String query = inlineQuery.getQuery();

            System.out.println("query = " + query);

            AnswerInlineQuery answerInlineQuery = new AnswerInlineQuery();
            answerInlineQuery.setInlineQueryId(inlineQuery.getId());
            List<InlineQueryResult> results = new ArrayList<>();

            for (int i = 0; i < 10; i++) {
                InlineQueryResultArticle article = buildInlineQueryResultArticle(i);
                results.add(article);
            }

            answerInlineQuery.setResults(results);

            try {
                execute(answerInlineQuery);
            } catch (TelegramApiException e) {
                LOGGER.error("Error occurred while processing request");
            }


            //answerInlineQuery
            //Use this method to send answers to an inline query. On success, True is returned.
            //No more than 50 results per query are allowed.
            //https://core.telegram.org/bots/api#answerinlinequery

        } else if (update.hasEditedMessage()) {
            System.out.println("update.hasEditedMessage() = " + update.hasEditedMessage());
            Message editedMessage = update.getEditedMessage();
            user = editedMessage.getFrom();

            String text = editedMessage.getText();

            System.out.println("text = " + text);
        } else if (update.hasCallbackQuery()) {
            user = callbackQuery.getFrom();

            System.out.println("callbackQuery.getMessage().getText() = " + callbackQuery.getMessage().getText());
        }

        Integer userId = user.getId();
        String firstName = user.getFirstName();
        String userName = user.getUserName();

        System.out.println("userId = " + userId);
        System.out.println("firstName = " + firstName);
        System.out.println("userName = " + userName);

    }

    @NotNull
    private InlineQueryResultArticle buildInlineQueryResultArticle(int idx) {
        InputTextMessageContent messageContent = new InputTextMessageContent();
        messageContent.disableWebPagePreview();
        messageContent.enableMarkdown(true);
        messageContent.setMessageText("foo bar " + idx);
        InlineQueryResultArticle article = new InlineQueryResultArticle();
        article.setInputMessageContent(messageContent);
        article.setId(Integer.toString(idx));
        article.setTitle("foo title" + idx);
        article.setDescription("foo descr" + idx);
        return article;
    }

    @Override
    public String getBotUsername() {
        return "IHInlineTestBot";
    }

    @Override
    public String getBotToken() {
        return "993335674:AAFvhh94TGryvE0ViKLDGRj8wsyOYFS8XR0";
    }
}
