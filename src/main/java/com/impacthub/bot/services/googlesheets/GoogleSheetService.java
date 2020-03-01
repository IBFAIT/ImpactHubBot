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
import com.impacthub.bot.services.Service;
import com.impacthub.bot.services.ServiceException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.List;

import static com.google.api.services.sheets.v4.SheetsScopes.SPREADSHEETS;
import static com.impacthub.bot.services.googlesheets.Constants.PHONE;
import static java.util.List.of;

public class GoogleSheetService implements Service {
    private final static String APPLICATION_NAME = "Google Sheets Example";
    private static Sheets sheetsService;

    private final Credential credential;
    // AVI: private final String spreadSheetId = "1lRMtnii4Ys7q_UzNLPkA86fVcXhYkznl6SGvuOpn4SM";
    private final String spreadSheetId;

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
    
    public String getFieldValue(int rowNum, String columnName) throws IOException, GeneralSecurityException {
        return getFieldValue(rowNum, getColNumByColName(columnName));
    }

    public int getColNumByColName(String columnName) throws IOException, GeneralSecurityException {
        List<List<Object>> values = readContent();

        List<Object> list = values.get(0);
        for (int i = 0; i < list.size(); i++) {
            Object o = list.get(i);
            if (columnName.equals(o))
                return i;
        }
        throw new IllegalStateException("No column " + columnName);
    }

    public String getFieldValue(int rowNum, int columnNum) throws IOException, GeneralSecurityException {
        List<List<Object>> values = readContent();
        List<Object> row = values.get(rowNum);

        return (String) row.get(columnNum);
    }

    public boolean isAuthorised(String phoneNumber) throws ServiceException {

        try {
            List<List<Object>> values = readContent();

            for (List row : values) {

                int colNum = getColNumByColName(PHONE);
                String field = (String)row.get(colNum);

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

    public Sheets getSheetsService() throws IOException, GeneralSecurityException {
        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public List<List<Object>> readContent() throws IOException, GeneralSecurityException {
        sheetsService = getSheetsService();

        //todo refactor
        String range = "Users";             // For reading entire sheet

        ValueRange response = sheetsService.spreadsheets().values()
                .get(spreadSheetId, range)
                .execute();

        return response.getValues();
    }
}
