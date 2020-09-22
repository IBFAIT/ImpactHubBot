package com.impacthub.bot.bots;

import com.impacthub.bot.services.authorisation.AuthorisationService;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConciergeTest {

    @Test
    public void getPhoneNumberFromUserId() {
        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");

        Concierge concierge = (Concierge) context.getBean("conciergeBot");
        AuthorisationService authService = (AuthorisationService) context.getBean("authorisationService");

        authService.registerUser(4242, "2424");

        String phoneNumber = concierge.getPhoneNumberFromUserId(4242);
        assertEquals("2424", phoneNumber);
    }
}