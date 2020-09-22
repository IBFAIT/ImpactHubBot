package com.impacthub.bot.services.db;

public interface Database {
  void put(int telegramUserId, String phoneNumber);

  String getPhoneNumberFromUserId(int telegramUserId);
}
