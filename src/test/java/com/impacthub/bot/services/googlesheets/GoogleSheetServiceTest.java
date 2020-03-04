package com.impacthub.bot.services.googlesheets;

import com.impacthub.bot.services.ServiceException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static java.lang.Boolean.parseBoolean;

public class GoogleSheetServiceTest {
    private String spreadSheetId = "16tB4m6WEiJLN3MGstkAWGCD4D_MdxPR8jJOmJRkPZtA";

    @Test
    public void isAuthorisedByPhoneNumber() {
        GoogleSheetService googleSheetService;
        try {
            googleSheetService = new GoogleSheetService(spreadSheetId);

            final String authorizedPhoneNumber = "+41 79 324 23 84";
            Assert.assertTrue(googleSheetService.isAuthorised(authorizedPhoneNumber));

            final String unAuthorizedPhoneNumber = "+41 79 000 00 00";
            Assert.assertFalse(googleSheetService.isAuthorised(unAuthorizedPhoneNumber));

        } catch (ServiceException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void getFieldValue() {

        GoogleSheetService googleSheetService;

        try {
            googleSheetService = new GoogleSheetService(spreadSheetId);
            String fieldValue = googleSheetService.getFieldValue(1, Columns.MEMBER_ID.getColNum());
            Assert.assertEquals("34421", fieldValue);

            fieldValue = googleSheetService.getFieldValue(3, Columns.IH_MEMBERSHIP.getColNum());
            Assert.assertEquals("Nomad", fieldValue);

            fieldValue = googleSheetService.getFieldValue(3, Columns.BOT_ADMIN.getColNum());
            Assert.assertFalse(parseBoolean(fieldValue));

        } catch (ServiceException | IOException | GeneralSecurityException e) {
            Assert.fail(e.getMessage());
        }

    }

    @Test
    public void getColNumByColName() throws ServiceException, IOException, GeneralSecurityException {

        GoogleSheetService googleSheetService;

        googleSheetService = new GoogleSheetService(spreadSheetId);

        Assert.assertEquals(0, Columns.MEMBER_ID.getColNum());
        Assert.assertEquals(5, Columns.TELEGRAM_USER_NAME.getColNum());
    }
}