package com.impacthub.bot.bots;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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

    private static final Logger log = LoggerFactory.getLogger(IHInlineTestBot.class);

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Message channelPost = update.getChannelPost();
        ChosenInlineQuery chosenInlineQuery = update.getChosenInlineQuery();
        InlineQuery inlineQuery = update.getInlineQuery();
        Integer updateId = update.getUpdateId();

        log(message, callbackQuery, channelPost, chosenInlineQuery, inlineQuery, updateId);

        User user = null;
        if (update.hasMessage()) {
            log.debug("update.hasMessage() = " + update.hasMessage());

            user = message.getFrom();

            Integer messageId = message.getMessageId();
            log.debug("messageId = " + messageId);

            String text = message.getText();

            log.debug("text = " + text);

            String msg = "*bold \\*text*\n" +
                    "_italic \\*text_\n" +
                    "__underline__\n" +
                    "~strikethrough~\n" +
                    "*bold _italic bold ~italic bold strikethrough~ __underline italic bold___ bold*\n" +
                    "[inline URL](http://www.example.com/)\n" +
                    "[inline mention of a user](tg://user?id=123456789)\n" +
                    "`inline fixed-width code`\n" +
                    "```\n" +
                    "pre-formatted fixed-width code block\n" +
                    "```\n" +
                    "```python\n" +
                    "pre-formatted fixed-width code block written in the Python programming language\n" +
                    "```";

            SendMessage sendMessage = new SendMessage(update.getMessage().getChat().getId(), msg);
            sendMessage.setParseMode("MarkdownV2");
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        } else if (update.hasInlineQuery()) {

            log.debug("update.hasInlineQuery() = " + update.hasInlineQuery());

            user = inlineQuery.getFrom();

            String query = inlineQuery.getQuery();

            log.debug("query = " + query);

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
                log.error("Error occurred while processing request", e);
            }


            //answerInlineQuery
            //Use this method to send answers to an inline query. On success, True is returned.
            //No more than 50 results per query are allowed.
            //https://core.telegram.org/bots/api#answerinlinequery

        } else if (update.hasEditedMessage()) {
            log.debug("update.hasEditedMessage() = " + update.hasEditedMessage());
            Message editedMessage = update.getEditedMessage();
            user = editedMessage.getFrom();

            String text = editedMessage.getText();

            log.debug("text = " + text);
        } else if (update.hasCallbackQuery()) {
            user = callbackQuery.getFrom();

            log.debug("callbackQuery.getMessage().getText() = " + callbackQuery.getMessage().getText());
        }

        Integer userId = user.getId();
        String firstName = user.getFirstName();
        String userName = user.getUserName();

        log.debug("userId = " + userId);
        log.debug("firstName = " + firstName);
        log.debug("userName = " + userName);

    }

    private void log(Message message, CallbackQuery callbackQuery, Message channelPost, ChosenInlineQuery chosenInlineQuery, InlineQuery inlineQuery, Integer updateId) {
        log.debug("message = " + message);
        log.debug("callbackQuery = " + callbackQuery);
        log.debug("channelPost = " + channelPost);
        log.debug("chosenInlineQuery = " + chosenInlineQuery);
        log.debug("inlineQuery = " + inlineQuery);
        log.debug("updateId = " + updateId);
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
