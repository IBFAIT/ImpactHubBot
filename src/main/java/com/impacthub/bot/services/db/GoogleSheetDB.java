package com.impacthub.bot.services.db;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.impacthub.bot.services.googlesheets.GoogleSheetService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import static com.impacthub.bot.services.googlesheets.GoogleSheetService.usersRange;
import static java.util.Arrays.asList;

public class GoogleSheetDB implements Database {

  private final GoogleSheetService googleSheetService;

  public GoogleSheetDB(GoogleSheetService googleSheetService) {
    this.googleSheetService = googleSheetService;
  }

  @Override
  public void put(int telegramUserId, String phoneNumber) {

    String phoneinDB = googleSheetService.getPhoneNumberFromTelegramUserId(telegramUserId);

    if (phoneinDB != null) {
      if (phoneinDB.equals(phoneNumber)) {
        System.out.println("everything is alright. mapping is already there");
      } else {
        System.out.println("conflict!");
        System.out.println("phoneNumber = " + phoneNumber);
        System.out.println("phoneinDB = " + phoneinDB);
      }
    } else {

      Sheets sheetsService = null;
      try {
        System.out.println("append " + phoneNumber + " -> " + telegramUserId);
        sheetsService = googleSheetService.getSheetsService();

        ValueRange content = new ValueRange();

        content.setValues(List.of(
                asList(
                        "",
                        telegramUserId,
                        "",
                        "",
                        "",
                        "=\"" + phoneNumber + "\"")));

        sheetsService.spreadsheets().values()
                .append("16tB4m6WEiJLN3MGstkAWGCD4D_MdxPR8jJOmJRkPZtA", usersRange, content)
                .setValueInputOption("USER_ENTERED")
                .setInsertDataOption("INSERT_ROWS")
                .setIncludeValuesInResponse(true)
                .execute();

      } catch (IOException | GeneralSecurityException e) {
        e.printStackTrace();
      }


    }
  }

  @Override
  public String getPhoneNumberFromUserId(int userID) {

    return googleSheetService.getPhoneNumberFromTelegramUserId(userID);
  }

}
