package com.impacthub.server;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

public class AccessSheets {

    private static Sheets sheetsService;
    private static String APPLICATION_NAME = "Google Sheets Example";
    private static String SPREADSHEET_ID = "1lRMtnii4Ys7q_UzNLPkA86fVcXhYkznl6SGvuOpn4SM";  // Set you SheetId
    private static List<List<Object>> values;

    public static Credential authorize() throws IOException, GeneralSecurityException {
        InputStream in = AccessSheets.class.getResourceAsStream("/credentials.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                JacksonFactory.getDefaultInstance(), new InputStreamReader(in));

        List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(),
                clientSecrets, scopes)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
                .setAccessType("offline")
                .build();

        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver())
                .authorize("user");
        return credential;

    }

    public static Sheets getSheetsService() throws IOException, GeneralSecurityException {
        Credential credential = authorize();
        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static List<List<Object>> connect() throws IOException, GeneralSecurityException {
        sheetsService = getSheetsService();
        String range = "Users";             // For reading entire sheet

        ValueRange response = sheetsService.spreadsheets().values()
                .get(SPREADSHEET_ID, range)
                .execute();

        values = response.getValues();
        return  values;
    }

    public static void fetchWithNumber(String number) throws IOException, GeneralSecurityException {
        //  number = "41793542420";     // For dynamically call
        values = connect();

        String pattern = "[^0-9]";
        for (List row : values) {
            if (row.get(5)
                    .toString()
                    .replaceAll(pattern, "")
                    .equals(number)){
                System.out.println("\nThe record with number "+number+
                        " is \nFirstName: "+row.get(2)+
                        ", LastName: "+row.get(3)+
                        ", Email: "+row.get(4)+
                        ", BlockedFlag: "+row.get(10));
            }
        }
    }

    public static void fetchWithAdminFlag(String flag) throws IOException, GeneralSecurityException {
        // flag = "TRUE";        // For dynamic call
        values = connect();

        System.out.println("\nRecords with Admin FLag set : ");
        for (List row : values) {
            if (row.get(9).toString().equals(flag)){
                System.out.println(("FirstName: "+row.get(2)+
                        ", LastName: "+row.get(3)+
                        ", Email: " + row.get(4)+
                        ", BlockedFlag: "+row.get(10)+
                        ", PhoneNumber: "+row.get(5)));
            }
        }
    }

    public static void writeUserToSheet() throws IOException, GeneralSecurityException {
        values = connect();

        // Hardcoding data temporarily
        ValueRange appendBody = new ValueRange()
                .setValues(Arrays.asList(
                        Arrays.asList("123456789","34426","Carl","Smith","carlsmith@gmail.com","+41793542720","@wickedcarl",
                                "Corporate","","TRUE","TRUE")
                ));

        AppendValuesResponse appendResult = sheetsService.spreadsheets().values()
                .append(SPREADSHEET_ID, "Users", appendBody)
                .setValueInputOption("USER_ENTERED")
                .setInsertDataOption("INSERT_ROWS")
                .setIncludeValuesInResponse(true)
                .execute();
    }

    public static void updateUserMembership(String ih_id) throws IOException, GeneralSecurityException {
        values = connect();
        int rowcount = 1;
        // ih_id = "34423" ;        // for dynamic call

        // Hardcoding values temporarily
        ValueRange body = new ValueRange()
                .setValues(Arrays.asList(
                        Arrays.asList("Community")
                ));

        // Finding the User by ih_id
        for (List row : values) {
            if (row.get(1).toString().equals(ih_id)){
                break;
            }
            rowcount++;
        }

        // Updating the Membership for the User
        String columnId = "G"+rowcount;
        UpdateValuesResponse result = sheetsService.spreadsheets().values()
                .update(SPREADSHEET_ID, columnId, body)
                .setValueInputOption("RAW")
                .execute();
    }

    public static List validateByTelegramId(Integer telegram_id) throws IOException, GeneralSecurityException {

        values = connect();
        for (List row : values) {
            if (row.get(0)
                    .toString()
                    .equals(telegram_id.toString())){
                return row;
            }
        }
        return null;
    }
}
