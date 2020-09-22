package com.impacthub.bot.services.sentinel;

import com.impacthub.bot.services.ServiceException;
import com.impacthub.bot.services.googlesheets.GoogleSheetService;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class GoogleSheetLoggingService implements SentinelService {
  private final GoogleSheetService googleSheetService;

  public GoogleSheetLoggingService(GoogleSheetService googleSheetService) {
    this.googleSheetService = googleSheetService;
  }

  @Override
  public void processUpdate(Record record) throws ServiceException {
    try {
      googleSheetService.append(record);
    } catch (IOException | GeneralSecurityException e) {
      throw new ServiceException(e);
    }
  }
}