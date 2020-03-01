package com.impacthub.bot.services.googlesheets;

import com.impacthub.bot.services.ServiceException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static com.impacthub.bot.services.googlesheets.Constants.*;
import static java.lang.Boolean.parseBoolean;
import static org.junit.jupiter.api.Assertions.*;

public class GoogleSheetServiceTest {
    private String spreadSheetId = "16tB4m6WEiJLN3MGstkAWGCD4D_MdxPR8jJOmJRkPZtA";

    @Test
    void isAuthorisedByPhoneNumber() {
        GoogleSheetService googleSheetService;
        try {
            googleSheetService = new GoogleSheetService(spreadSheetId);

            final String authorizedPhoneNumber = "+41 79 324 23 84";
            assertTrue(googleSheetService.isAuthorised(authorizedPhoneNumber));

            final String unAuthorizedPhoneNumber = "+41 79 000 00 00";
            assertFalse(googleSheetService.isAuthorised(unAuthorizedPhoneNumber));

        } catch (ServiceException e) {
            fail(e);
        }
    }

    @Test
    void getFieldValue() {

        GoogleSheetService googleSheetService;

        try {
            googleSheetService = new GoogleSheetService(spreadSheetId);
            String fieldValue = googleSheetService.getFieldValue(1, MEMBER_ID);
            assertEquals("34421", fieldValue);

            fieldValue = googleSheetService.getFieldValue(3, IH_MEMBERSHIP);
            assertEquals("Nomad", fieldValue);

            fieldValue = googleSheetService.getFieldValue(3, BOT_ADMIN);
            assertFalse(parseBoolean(fieldValue));

        } catch (ServiceException | IOException | GeneralSecurityException e) {
            fail(e);
        }

    }

    @Test
    void getColNumByColName() throws ServiceException, IOException, GeneralSecurityException {

        GoogleSheetService googleSheetService;

        googleSheetService = new GoogleSheetService(spreadSheetId);

        assertEquals(0, googleSheetService.getColNumByColName(MEMBER_ID));
        assertEquals(5, googleSheetService.getColNumByColName(TELEGRAM_USER_NAME));
    }
}