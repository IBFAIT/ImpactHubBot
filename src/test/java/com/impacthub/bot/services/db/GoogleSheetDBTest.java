package com.impacthub.bot.services.db;

import com.impacthub.bot.services.googlesheets.GoogleSheetService;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GoogleSheetDBTest {

  private static final int telegramUserId = 42;
  private static final String phoneNumber = "+41 79 324 23 84";

  @Test
  public void putAndGet() throws IOException {
    GoogleSheetService googleSheetService = new GoogleSheetService("16tB4m6WEiJLN3MGstkAWGCD4D_MdxPR8jJOmJRkPZtA");
    final Database db = new GoogleSheetDB(googleSheetService);

    db.put(telegramUserId, phoneNumber);

    final String phoneNumber = db.getPhoneNumberFromUserId(telegramUserId);

    assertEquals(GoogleSheetDBTest.phoneNumber, phoneNumber);
  }

  //todo: test different scenarios:
  // * mapping is already there
  // * no mapping
  // * conflict...
  @Test
  public void getPhoneNumberFromTelegramUserId() throws IOException {
    final GoogleSheetService googleSheetService = new GoogleSheetService("16tB4m6WEiJLN3MGstkAWGCD4D_MdxPR8jJOmJRkPZtA");
    final GoogleSheetDB db = new GoogleSheetDB(googleSheetService);
    String phoneNumber = db.getPhoneNumberFromUserId(1063373308);
    assertEquals("‭+41 77 504 66 11‬", phoneNumber);
  }
}