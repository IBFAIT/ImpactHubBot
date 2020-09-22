package com.impacthub.bot.services.sentinel;

import com.impacthub.bot.services.googlesheets.GoogleSheetService;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GoogleSheetSL4JLoggingServiceTest {

  @Test
  void digestMessage() throws IOException, GeneralSecurityException {

    GoogleSheetService service = new GoogleSheetService("16tB4m6WEiJLN3MGstkAWGCD4D_MdxPR8jJOmJRkPZtA");

    final SentinelService sentinelService = new GoogleSheetLoggingService(service);

    Update update = mock(Update.class);
    Message message = mock(Message.class);
    User user = mock(User.class);

    when(update.getMessage()).thenReturn(message);

    when(message.getFrom()).thenReturn(user);

    when(message.getText()).thenReturn("lorem ipsum");
    when(message.getChatId()).thenReturn(42L);

    when(user.getId()).thenReturn(21);


    //assert: neuer eintrag im google sheet
  }

  @Test
  void digestChat() {
  }

  @Test
  void digestContact() {
  }

  @Test
  void digestTextMessage() {
  }

  @Test
  void digestUser() {
  }

  @Test
  void digestInlineQuery() {
  }

  @Test
  void digestCallbackQuery() {
  }

  @Test
  void digestChannelPost() {
  }

  @Test
  void digestChosenInlineQuery() {
  }

  @Test
  void digestUpdate() {
  }
}