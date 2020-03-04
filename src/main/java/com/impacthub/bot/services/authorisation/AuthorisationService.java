package com.impacthub.bot.services.authorisation;

import com.impacthub.bot.services.Service;
import com.impacthub.bot.services.ServiceException;
import com.impacthub.bot.services.googlesheets.GoogleSheetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthorisationService implements Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorisationService.class);

    private GoogleSheetService googleSheetService;

    public AuthorisationService(GoogleSheetService googleSheetService) {
        super();
        this.googleSheetService=googleSheetService;
    }

    public boolean isAuthorised(String phoneNumber) throws ServiceException {
        LOGGER.info("Authorising by phone number : {}", phoneNumber);
        return googleSheetService.isAuthorised(phoneNumber);
    }

    public String getMembership(String phoneNumber) {
        return googleSheetService.getMembership(phoneNumber);
    }

}