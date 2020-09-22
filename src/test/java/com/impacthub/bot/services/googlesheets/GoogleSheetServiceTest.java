package com.impacthub.bot.services.googlesheets;

import com.impacthub.bot.services.ServiceException;
import com.impacthub.bot.services.sentinel.Record;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static com.impacthub.bot.services.googlesheets.GoogleSheetService.usersRange;
import static java.lang.Boolean.parseBoolean;
import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.*;

public class GoogleSheetServiceTest {

    @Test
    public void isAuthorisedByPhoneNumber() {

        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        GoogleSheetService googleSheetService = (GoogleSheetService) context.getBean("googleSheetService");

        try {
            final String authorizedPhoneNumber = "+41 79 324 23 84";
            assertTrue(googleSheetService.isAuthorised(authorizedPhoneNumber));

            final String unAuthorizedPhoneNumber = "+41 79 000 00 00";
            assertFalse(googleSheetService.isAuthorised(unAuthorizedPhoneNumber));

        } catch (ServiceException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getFieldValue() {

        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        GoogleSheetService googleSheetService = (GoogleSheetService) context.getBean("googleSheetService");

        try {
            String fieldValue = googleSheetService.getFieldValue(1, Columns.MEMBER_ID.getColNum(), usersRange);
            assertEquals("34421", fieldValue);

            fieldValue = googleSheetService.getFieldValue(3, Columns.IH_MEMBERSHIP.getColNum(), usersRange);
            assertEquals("Nomad", fieldValue);

            fieldValue = googleSheetService.getFieldValue(3, Columns.BOT_ADMIN.getColNum(), usersRange);
            assertFalse(parseBoolean(fieldValue));

        } catch (IOException | GeneralSecurityException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void getColNumByColName() {
        assertEquals(0, Columns.MEMBER_ID.getColNum());
        assertEquals(6, Columns.TELEGRAM_USER_NAME.getColNum());
    }

    @Test
    public void testContains() {
        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        GoogleSheetService googleSheetService = (GoogleSheetService) context.getBean("googleSheetService");

        boolean b = googleSheetService.contains("foo", usersRange);
        assertFalse(b);

        b = googleSheetService.contains("+41 79 354 24 20", usersRange);
        assertTrue(b);
    }

    @Test
    public void testPersist() throws IOException, GeneralSecurityException {
        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        GoogleSheetService googleSheetService = (GoogleSheetService) context.getBean("googleSheetService");

        Record record = new Record();

        record.setLocalDateTime(now());
        record.setUpdateId("6");
        record.setChatId("42");
        record.setTitle("Test Group");
        record.setSenderId("-1");
        record.setUserName("junit");
        record.setMessage("Lorem Ipsum ..");

        googleSheetService.append(record);

    }


    //todo
    @Test
    public void getMembershipByUserId() {
    }

}