package com.impacthub.bot.services.googlesheets;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.impacthub.bot.services.Constants;
import com.impacthub.bot.services.Service;
import com.impacthub.bot.services.ServiceException;
import com.impacthub.bot.services.sentinel.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.impacthub.bot.services.Constants.DEFAULT_MEMBERSHIP;
import static com.impacthub.bot.services.googlesheets.Columns.*;
import static java.util.Arrays.asList;
import static java.util.List.of;

/**
 * Services for DB interaction
 */
public class GoogleSheetService implements Service {

  private static final Logger log = LoggerFactory.getLogger(GoogleSheetService.class);

  private static final String regex = "[\\u202C\\uD83D\\uFFFD\\uFE0F\\u203C\\u3010]";

  public static String usersRange = "Users";
  public static String sentinelRange = "Sentinel";

  private final Credential credential;
  private final String spreadSheetId;


  /**
   * Create Service to Connect to Google SpreadSheet.
   *
   * @param spreadSheetId Spreadheet's ID
   */
  public GoogleSheetService(String spreadSheetId) throws IOException {
    super();
    this.spreadSheetId = spreadSheetId;

    InputStream is = GoogleSheetService.class.getResourceAsStream("/service-account-credentials.json");
    credential = GoogleCredential.fromStream(is).createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));

  }

  /**
   * Get Field Value
   *
   * @param rowNum    Row number in the Google SpreadSheet
   * @param columnNum Column number in the Google SpreadSheet
   * @return String FieldValue
   */
  public String getFieldValue(int rowNum, int columnNum, String range) throws IOException, GeneralSecurityException {
    List<List<Object>> values = readContent(range);
    List<Object> row = values.get(rowNum);

    return (String) row.get(columnNum);
  }


//todo: Authorization is based on ih membership ... if a user has no membership or membership== none -> not authorized || authorized

  /**
   * Get User's authorization status based on the provided User's phone number
   *
   * @param phoneNumber phone number of the User
   * @return Boolean Authorization status
   */
  public boolean isAuthorised(String phoneNumber) throws ServiceException {

    try {
      List<List<Object>> values = readContent(usersRange);

      for (List row : values) {

        int colNum = PHONE.getColNum();
        String field = (String) row.get(colNum);

        String pattern = "[^0-9]";
        String stripped1 = phoneNumber.replaceAll(pattern, "");
        String stripped2 = field.replaceAll(pattern, "");

        if (stripped1.equals(stripped2))
          return true;

      }
      return false;

    } catch (IOException | GeneralSecurityException e) {
      throw new ServiceException(e);
    }
  }


  /**
   * Get active membership of User
   *
   * @param userId User's Id
   * @return String Active Membership
   */
  //todo
  public String getMembership(int userId) {

    try {
      List<List<Object>> values = readContent(usersRange);
      for (List row : values) {

        int colNum = USER_ID.getColNum();

        String field = (String) row.get(colNum);

        if (Integer.toString(userId).equals(field)) {
          return row.get(IH_MEMBERSHIP.getColNum()).toString();
        }
      }
    } catch (GeneralSecurityException | IOException e) {
      log.error("Error while getting Membership.", e);
    }

    return DEFAULT_MEMBERSHIP;
  }


  /**
   * Get active membership of User
   *
   * @param phoneNumber User's phone number
   * @return String Active Membership of the User
   */
  public String getMembership(String phoneNumber) {

    try {
      List<List<Object>> values = readContent(usersRange);
      for (List row : values) {

        int colNum = PHONE.getColNum();
        String field = (String) row.get(colNum);

        String pattern = "[^0-9]";
        String stripped1 = phoneNumber.replaceAll(pattern, "");
        String stripped2 = field.replaceAll(pattern, "");

        if (stripped1.equals(stripped2)) {
          return row.get(IH_MEMBERSHIP.getColNum()).toString();
        }
      }
    } catch (GeneralSecurityException | IOException e) {
      log.error("Error while getting Membership.", e);
    }

    return DEFAULT_MEMBERSHIP;
  }


  /**
   * Retuns an instance of GoogleSheet Service
   *
   * @return An instance of GoogleSheet Service
   */
  public Sheets getSheetsService() throws IOException, GeneralSecurityException {
    return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
            JacksonFactory.getDefaultInstance(), credential)
            .setApplicationName(Constants.APPLICATION_NAME)
            .build();
  }


  /**
   * Fetch List of Values from Google SpreadSheet
   *
   * @return {@code List<List<Object>>}
   */
  public List<List<Object>> readContent(String range) throws IOException, GeneralSecurityException {
    Sheets sheetsService = getSheetsService();

    ValueRange response = sheetsService.spreadsheets().values()
            .get(spreadSheetId, range)
            .execute();

    return response.getValues();
  }


  /**
   * Get Field Value
   *
   * @param phoneNumber User's phone number
   * @return Date Membership Expiration Date
   */
  //todo
  public Date getMembershipExpirationDate(String phoneNumber) {
    return new Date();
  }


  /**
   * Get Field Value
   *
   * @param userId User's ID
   * @return Date Membership Expiration Date
   */
  //todo
  public Date getMembershipExpirationDate(int userId) {
    return new Date();
  }

  public boolean contains(String searchString, String range) {
    try {
      List<List<Object>> lists = readContent(range);

      for (List<Object> objects : lists) {
        for (Object o : objects) {

          if (searchString.trim().equals(o.toString().trim().replaceAll(regex, "")))
            return true;
        }
      }
      return false;
    } catch (IOException | GeneralSecurityException e) {
      return false;
    }
  }

  public void append(Record record) throws IOException, GeneralSecurityException {

    Sheets sheetsService = getSheetsService();

    ValueRange content = new ValueRange();

    content.setValues(of(
            asList(
                    record.getLocalDateTime().toString(),
                    record.getUpdateId(),
                    record.getChatId(),
                    record.getTitle(),
                    record.getSenderId(),
                    record.getUserName(),
                    record.getFirstName(),
                    record.getMessage())));

    sheetsService.spreadsheets().values()
            .append(spreadSheetId, sentinelRange, content)
            .setValueInputOption("USER_ENTERED")
            .setInsertDataOption("INSERT_ROWS")
            .setIncludeValuesInResponse(true)
            .execute();
  }

  public String getPhoneNumberFromTelegramUserId(int telegramUserId) {


    try {
      List<List<Object>> values = readContent(usersRange);
      for (List row : values) {

        int colNum = TELEGRAM_ID.getColNum();
        String field = (String) row.get(colNum);

        if (Integer.toString(telegramUserId).equals(field)) {
          return row.get(PHONE.getColNum()).toString();
        }
      }
    } catch (GeneralSecurityException | IOException e) {
      log.error("Error while getting phone number.", e);
    }

    return null;
  }

  public boolean userInDB(int userId) {
    List<List<Object>> values = null;

    try {
      values = readContent(usersRange);

      for (List<Object> row : values) {

        int colNum = TELEGRAM_ID.getColNum();
        String field = (String) row.get(colNum);

        if (Integer.toString(userId).equals(field)) {
          return
                  true;
        }
      }

    } catch (IOException | GeneralSecurityException e) {
      e.printStackTrace();
    }
    return false;
  }
}