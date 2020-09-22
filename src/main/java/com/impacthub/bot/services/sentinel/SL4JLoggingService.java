package com.impacthub.bot.services.sentinel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SL4JLoggingService implements SentinelService {

  private static final Logger log = LoggerFactory.getLogger(SL4JLoggingService.class);

  public void addEmptyLines() {
    log.debug("      ");
    log.debug("      ");
    log.debug("      ");
  }

  public void processUpdate(Record record) {

    log.debug("Update w/ id " + record.getUpdateId() + " received.");

    log.debug("   channelPost = " + record.getChannelPost());
    log.debug("   chosenInlineQuery = " + record.getChosenInlineQuery());
    log.debug("   chatId = " + record.getChatId());
    log.debug("   chatTitle = " + record.getTitle());
    log.debug("   chatInviteLink = " + record.getInviteLink());

    log.debug("   groupChat = " + record.isGroupChat());
    log.debug("   superGroupChat = " + record.isSuperGroupChat());
    log.debug("   channelChat = " + record.isChannelChat());
    log.debug("   userChat = " + record.isUserChat());

    log.debug("   senderID = " + record.getSenderId());
    log.debug("   phoneNumber = " + record.getPhoneNumber());
    log.debug("   message: " + record.getMessage());

    log.debug("   firstName = " + record.getFirstName());
    log.debug("   lastName = " + record.getLastName());
    log.debug("   userName = " + record.getUserName());
    log.debug("   isBot = " + record.isBot());

    addEmptyLines();
  }
}