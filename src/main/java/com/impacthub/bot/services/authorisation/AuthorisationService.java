package com.impacthub.bot.services.authorisation;

import com.impacthub.bot.services.Service;
import com.impacthub.bot.services.ServiceException;
import com.impacthub.bot.services.googlesheets.GoogleSheetService;

public class AuthorisationService implements Service {
    private GoogleSheetService googleSheetService;

    public AuthorisationService(GoogleSheetService googleSheetService) {
        super();
        this.googleSheetService=googleSheetService;
    }


    public GoogleSheetService getGoogleSheetService() {
        return googleSheetService;
    }

    public void setGoogleSheetService(GoogleSheetService googleSheetService) {
        this.googleSheetService = googleSheetService;
    }

    public boolean isAuthorised(String phoneNumber) throws ServiceException {
        return googleSheetService.isAuthorised(phoneNumber);
    }

}
