package com.impacthub.bot.bots;

import com.impacthub.bot.services.sentinel.SL4JLoggingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class BouncerTest {

  @Test
  void onUpdateReceived() throws TelegramApiException {

    Bouncer bot = mock(Bouncer.class);

    doCallRealMethod().when(bot).onUpdatesReceived(any());

    Update update1 = new Update();
    Update update2 = new Update();

    bot.onUpdatesReceived(asList(update1, update2));

    verify(bot).onUpdateReceived(update1);
    verify(bot).onUpdateReceived(update2);


    SendMessage message = new SendMessage()
            .setChatId("@test")
            .setText("Hithere")
            .setReplyToMessageId(12)
            .setParseMode(ParseMode.HTML)
            .setReplyMarkup(new ForceReplyKeyboard());

    bot.execute(message);
  }

  @Test
  void getBotUsername() {
    Bouncer bouncer = new Bouncer(new SL4JLoggingService());
    Assertions.assertEquals("ImpactHubBouncerBot", bouncer.getBotUsername());
  }

  @Test
  void getBotToken() {
    Bouncer bouncer = new Bouncer(new SL4JLoggingService());
    Assertions.assertEquals("1074140532:AAHYXDQKgXnoQVaJ8LW66aTLrOyYJRKkve0", bouncer.getBotToken());
  }
}