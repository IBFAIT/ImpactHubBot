package com.impacthub.bot.bots;

import com.impacthub.bot.services.ServiceException;
import com.impacthub.bot.services.sentinel.Record;
import com.impacthub.bot.services.sentinel.RecordFactory;
import com.impacthub.bot.services.sentinel.SentinelService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bouncer extends TelegramLongPollingBot {

  private final List<SentinelService> services = new ArrayList<>();
  private final RecordFactory recordFactory = new RecordFactory();

  public Bouncer(SentinelService... service) {
    Collections.addAll(services, service);
  }

  @Override
  public void onUpdateReceived(Update update) {
    for (final SentinelService service : services) {
      final Record record = recordFactory.build(update);
      try {
        service.processUpdate(record);
      } catch (ServiceException e) {
        //todo
        e.printStackTrace();
      }
    }
  }

  //todo
  @Override
  public String getBotUsername() {
    return "ImpactHubBouncerBot";
  }

  @Override
  public String getBotToken() {
    return "1074140532:AAHYXDQKgXnoQVaJ8LW66aTLrOyYJRKkve0";
  }
}