package com.impacthub.bot.services.authorisation;

import com.impacthub.bot.services.ServiceException;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

class AuthorisationServiceTest {

    @Test
    void isAuthorisedByPhoneNumber() {

        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        AuthorisationService authorisationService = (AuthorisationService) context.getBean("authorisationService");

        try {

            final String authorizedPhoneNumber = "+41 79 324 23 84";
            assertTrue(authorisationService.isAuthorised(authorizedPhoneNumber));

            final String unAuthorizedPhoneNumber = "+41 79 000 00 00";
            assertFalse(authorisationService.isAuthorised(unAuthorizedPhoneNumber));

        } catch (ServiceException e) {
            fail(e);
        }
    }

    @Test
    void testRegex() {
        String pattern = "[^0-9]";
        String phoneNumber = "+41 79 324 23 84".replaceAll(pattern, "");
        assertEquals("41793242384", phoneNumber);
    }
}