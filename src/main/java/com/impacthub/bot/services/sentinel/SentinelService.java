package com.impacthub.bot.services.sentinel;

import com.impacthub.bot.services.ServiceException;

public interface SentinelService {
  void processUpdate(Record record) throws ServiceException;
}
