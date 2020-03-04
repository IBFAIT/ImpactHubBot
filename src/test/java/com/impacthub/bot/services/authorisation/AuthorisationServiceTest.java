package com.impacthub.bot.services.authorisation;

import com.impacthub.bot.services.ServiceException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class AuthorisationServiceTest {

    @Test
    public void isAuthorisedByPhoneNumber() {

        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        AuthorisationService authorisationService = (AuthorisationService) context.getBean("authorisationService");

        try {

            final String authorizedPhoneNumber = "+41 79 324 23 84";
            Assert.assertTrue(authorisationService.isAuthorised(authorizedPhoneNumber));

            final String unAuthorizedPhoneNumber = "+41 79 000 00 00";
            Assert.assertFalse(authorisationService.isAuthorised(unAuthorizedPhoneNumber));

        } catch (ServiceException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testRegex() {
        String pattern = "[^0-9]";
        String phoneNumber = "+41 79 324 23 84".replaceAll(pattern, "");
        Assert.assertEquals("41793242384", phoneNumber);
    }
}