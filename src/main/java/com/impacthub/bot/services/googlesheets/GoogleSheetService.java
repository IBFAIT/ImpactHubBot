package com.impacthub.bot.services.googlesheets;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.impacthub.bot.services.Constants;
import com.impacthub.bot.services.Service;
import com.impacthub.bot.services.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;

import static com.google.api.services.sheets.v4.SheetsScopes.SPREADSHEETS;
import static com.impacthub.bot.services.Constants.DEFAULT_MEMBERSHIP;
import static com.impacthub.bot.services.googlesheets.Columns.*;
import static java.util.List.of;

/**
 * Services for DB interaction
 */
public class GoogleSheetService implements Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleSheetService.class);

    private static Sheets sheetsService;

    private final Credential credential;
    private final String spreadSheetId;

    /**
     * Create Service to Connect to Google SpreadSheet. Create GoogleAuthorization flow using GoogleClientSecrets object
     *
     * @param spreadSheetId
     */
    public GoogleSheetService(String spreadSheetId) throws ServiceException {
        super();
        this.spreadSheetId = spreadSheetId;

        InputStream in = GoogleSheetService.class.getResourceAsStream("/credentials.json");
        GoogleClientSecrets clientSecrets;
        try {
            clientSecrets = GoogleClientSecrets.load(
                    JacksonFactory.getDefaultInstance(), new InputStreamReader(in));


            List<String> scopes = of(SPREADSHEETS);

            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(),
                    clientSecrets, scopes)
                    .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
                    .setAccessType("offline")
                    .build();


            credential = new AuthorizationCodeInstalledApp(
                    flow, new LocalServerReceiver())
                    .authorize("user");

        } catch (IOException | GeneralSecurityException e) {
            throw new ServiceException(e);
        }

    }


    /**
     * Get Field Value
     *
     * @param rowNum    Row number in the Google SpreadSheet
     * @param columnNum Column number in the Google SpreadSheet
     * @return String FieldValue
     */
    public String getFieldValue(int rowNum, int columnNum) throws IOException, GeneralSecurityException {
        List<List<Object>> values = readContent();
        List<Object> row = values.get(rowNum);

        return (String) row.get(columnNum);
    }


    /**
     * Get User's authorization status based on the provided User's phone number
     *
     * @param phoneNumber phone number of the User
     * @return Boolean Authorization status
     */
    public boolean isAuthorised(String phoneNumber) throws ServiceException {

        try {
            List<List<Object>> values = readContent();

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
            List<List<Object>> values = readContent();
            for (List row : values) {

                int colNum = USER_ID.getColNum();

                String field = (String) row.get(colNum);

                if (Integer.toString(userId).equals(field)) {
                    return row.get(IH_MEMBERSHIP.getColNum()).toString();
                }
            }
        } catch (GeneralSecurityException | IOException e) {
            LOGGER.error("Error while getting Membership.", e);
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
            List<List<Object>> values = readContent();
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
            LOGGER.error("Error while getting Membership.", e);
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
    public List<List<Object>> readContent() throws IOException, GeneralSecurityException {
        sheetsService = getSheetsService();

        //todo refactor
        String range = "Users";             // For reading entire sheet

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
}
