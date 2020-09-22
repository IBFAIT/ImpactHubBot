package com.impacthub.bot.services.authorisation;

import com.impacthub.bot.services.Service;
import com.impacthub.bot.services.ServiceException;
import com.impacthub.bot.services.db.Database;
import com.impacthub.bot.services.googlesheets.GoogleSheetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Services to authorize the User.
 */
public class AuthorisationService implements Service {

  private static final Logger LOGGER = LoggerFactory.getLogger(AuthorisationService.class);

  private final GoogleSheetService googleSheetService;
  private final Database db;

  public AuthorisationService(GoogleSheetService googleSheetService, Database db) {
    super();
    this.googleSheetService = googleSheetService;
    this.db = db;
  }

  /**
   * Returns a boolean based on User's authorization.
   *
   * @param phoneNumber User's phone number.
   * @return Boolean User's authorization status.
   */
  public boolean isAuthorised(String phoneNumber) throws ServiceException {
    LOGGER.info("Authorising by phone number : {}", phoneNumber);
    return googleSheetService.isAuthorised(phoneNumber);
  }

  /**
   * Returns User's active membership.
   *
   * @param phoneNumber User's phone number.
   * @return String User's active membership.
   */
  public String getMembership(String phoneNumber) {
    return googleSheetService.getMembership(phoneNumber);
  }


  /**
   * Returns User's phone number based on User's Id.
   *
   * @param userID User's ID.
   * @return String User's phone number.
   */
  public String getPhoneNumberFromUserId(int userID) {
    return db.getPhoneNumberFromUserId(userID);
  }


  /**
   * Saves user's details data into DB.
   *
   * @param userId      User's ID.
   * @param phoneNumber User's phone number.
   */
  public void registerUser(int userId, String phoneNumber) {
    db.put(userId, phoneNumber);
  }

  /**
   * Returns User's membership expiry date.
   *
   * @param phoneNumber User's phone number.
   * @return Date Membership expiry date.
   */
  public Date getMembershipExpirationDate(String phoneNumber) {
    return googleSheetService.getMembershipExpirationDate(phoneNumber);
  }


  /**
   * Returns User's membership expiry date.
   *
   * @param userId User's ID.
   * @return Date Membership expiry date.
   */
  public Date getMembershipExpirationDate(int userId) {
    return googleSheetService.getMembershipExpirationDate(userId);
  }

  public boolean userInDB(int userId) {
    return googleSheetService.userInDB(userId);
  }
}