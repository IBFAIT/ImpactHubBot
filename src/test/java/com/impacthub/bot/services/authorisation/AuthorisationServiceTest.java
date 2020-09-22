package com.impacthub.bot.services.authorisation;

import com.impacthub.bot.bots.Concierge;
import com.impacthub.bot.services.ServiceException;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


public class AuthorisationServiceTest {

  private final ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
  private final AuthorisationService authorisationService = (AuthorisationService) context.getBean("authorisationService");
  private final Concierge concierge = (Concierge) context.getBean("conciergeBot");

  @Test
  public void isAuthorisedByPhoneNumber() {

    try {

      final String authorizedPhoneNumber = "+41 79 324 23 84";
      assertTrue(authorisationService.isAuthorised(authorizedPhoneNumber));

      final String unAuthorizedPhoneNumber = "+41 79 000 00 00";
      assertFalse(authorisationService.isAuthorised(unAuthorizedPhoneNumber));

    } catch (ServiceException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testRegex() {
    String pattern = "[^0-9]";
    String phoneNumber = "+41 79 324 23 84".replaceAll(pattern, "");
    assertEquals("41793242384", phoneNumber);
  }


  @Test
  public void getMembership() {

    final String isMember = "+41 79 354 24 20";
    assertEquals("Community", authorisationService.getMembership(isMember));

    final String isNotMember = "+41 79 888 88 88";
    assertEquals("None", authorisationService.getMembership(isNotMember));

  }

  @Test
  public void getPhoneNumberFromUserId() {

    authorisationService.registerUser(185655, "+91 9689 60 42 38");

    String phoneNumber = concierge.getPhoneNumberFromUserId(185655);
    assertEquals("+91 9689 60 42 38", phoneNumber);
  }

  @Test
  public void getMembershipExpirationDate() throws ParseException {

    String rawTestDate = "25/12/2020";
    Date testDate = new SimpleDateFormat("dd/MM/yyyy").parse(rawTestDate);

    Date date = authorisationService.getMembershipExpirationDate("+41 79 354 24 20");
    int valid = testDate.compareTo(date);
    assertEquals(0, valid);

  }
}