package com.impacthub.bot.services.authorisation;

import com.impacthub.bot.services.Service;
import com.impacthub.bot.services.ServiceException;
import com.impacthub.bot.services.db.Database;
import com.impacthub.bot.services.googlesheets.GoogleSheetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class AuthorisationService implements Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorisationService.class);

    private final GoogleSheetService googleSheetService;
    private final Database db;

    public AuthorisationService(GoogleSheetService googleSheetService, Database db) {
        super();
        this.googleSheetService = googleSheetService;
        this.db = db;
    }

    public boolean isAuthorised(String phoneNumber) throws ServiceException {
        LOGGER.info("Authorising by phone number : {}", phoneNumber);
        return googleSheetService.isAuthorised(phoneNumber);
    }

    public String getMembership(String phoneNumber) {
        return googleSheetService.getMembership(phoneNumber);
    }

    public String getPhoneNumberFromUserId(int userID) {
        return db.getPhoneNumberFromUserId(userID);
    }

    public void registerUser(int userId, String phoneNumber) {
        db.put(userId, phoneNumber);
    }

    public Date getMembershipExpirationDate(String phoneNumber) {
        return googleSheetService.getMembershipExpirationDate(phoneNumber);
    }

    public Date getMembershipExpirationDate(int userId) {
        return googleSheetService.getMembershipExpirationDate(userId);
    }
}