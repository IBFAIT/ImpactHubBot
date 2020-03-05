package com.impacthub.bot.services.googlesheets;

import com.impacthub.bot.services.ServiceException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static java.lang.Boolean.parseBoolean;

public class GoogleSheetServiceTest {

    @Test
    public void isAuthorisedByPhoneNumber() {

        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        GoogleSheetService googleSheetService = (GoogleSheetService) context.getBean("googleSheetService");

        try {
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

        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        GoogleSheetService googleSheetService = (GoogleSheetService) context.getBean("googleSheetService");

        try {
            String fieldValue = googleSheetService.getFieldValue(1, Columns.MEMBER_ID.getColNum());
            Assert.assertEquals("34421", fieldValue);

            fieldValue = googleSheetService.getFieldValue(3, Columns.IH_MEMBERSHIP.getColNum());
            Assert.assertEquals("Nomad", fieldValue);

            fieldValue = googleSheetService.getFieldValue(3, Columns.BOT_ADMIN.getColNum());
            Assert.assertFalse(parseBoolean(fieldValue));

        } catch (IOException | GeneralSecurityException e) {
            Assert.fail(e.getMessage());
        }

    }

    @Test
    public void getColNumByColName() {

        Assert.assertEquals(0, Columns.MEMBER_ID.getColNum());
        Assert.assertEquals(5, Columns.TELEGRAM_USER_NAME.getColNum());
    }

    //todo
    @Test
    public void getMembershipByUserId() {
    }

}