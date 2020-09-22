package com.impacthub.bot.services.sentinel;

import com.impacthub.bot.services.ServiceException;
import com.impacthub.bot.services.authorisation.AuthorisationService;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

public class UserLookupService implements SentinelService {

  private AuthorisationService authorisationService;

  @Override
  public void processUpdate(Record record) throws ServiceException {

    List<User> newChatMembers = record.getNewChatMembers();

    System.out.println("New Chat member(s):");

    for (User user : newChatMembers) {
      System.out.println("- user = " + user);
      processLookup(user.getId());
    }

    String senderId = record.getSenderId();
    System.out.println("Processing lookup for " + record.getUserName() + " " + record.getFirstName() + " w/ userId " + record.getSenderId());
    processLookup(Integer.parseInt(senderId));

  }

  private void processLookup(int userId) throws ServiceException {

    boolean b = authorisationService.userInDB(userId);
    System.out.println("user in db = " + b);

    String phone = authorisationService.getPhoneNumberFromUserId(userId);

    if (phone != null) {
      boolean authorised = authorisationService.isAuthorised(phone);

      System.out.println("authorised = " + authorised);

      String membership = authorisationService.getMembership(phone);

      System.out.println("membership = " + membership);
    } else {
      System.out.println("Phone number is not available... can't do lookup");
    }
  }

  public void setAuthorisationService(AuthorisationService authService) {
    this.authorisationService = authService;
  }
}
