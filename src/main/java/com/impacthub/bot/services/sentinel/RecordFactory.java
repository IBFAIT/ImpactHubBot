package com.impacthub.bot.services.sentinel;

import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.games.Animation;
import org.telegram.telegrambots.meta.api.objects.inlinequery.ChosenInlineQuery;

import java.util.List;

import static java.time.LocalDateTime.now;

public class RecordFactory {

  public Record build(final Update update) {

    final Record record = new Record();

    processUpdateId(update, record);
    processLocalDateTime(update, record);
    processMessage(update, record);

    processChannelPost(update, record);
    processChosenInlineQuery(update, record);
    processNewChatMembers(update, record);

    return record;
  }

  private void processNewChatMembers(Update update, Record record) {
    List<User> newChatMembers = update.getMessage().getNewChatMembers();
    record.setNewChatMembers(newChatMembers);
  }

  private void processUpdateId(Update update, Record record) {
    final String updateId = update.getUpdateId().toString();
    record.setUpdateId(updateId);
  }

  private void processLocalDateTime(Update update, Record record) {
    record.setLocalDateTime(now());
  }

  public void processMessage(final Update update, Record record) {

    Message message;

    if (update.hasMessage())
      message = update.getMessage();
    else if (update.hasEditedMessage())
      message = update.getEditedMessage();
    else
      return;

    Long chatId = message.getChat().getId();
    String title = message.getChat().getTitle();
    Integer userId = message.getFrom().getId();
    String userName = message.getFrom().getUserName();
    String firstName = message.getFrom().getFirstName();
    String text = message.getText();

    boolean userMessage = message.isUserMessage();
    if (userMessage)
      title = "Priv. conversation w/ " + userName;


    if (update.hasEditedMessage())
      text = update.getEditedMessage().getText();

    record.setChatId(chatId.toString());
    record.setTitle(title);
    record.setSenderId(userId.toString());
    record.setUserName(userName);
    record.setFirstName(firstName);
    record.setMessage(text);

    processChat(message.getChat(), record);

    processUser(message, record);
    processTextMessage(message, record);
    processContact(message, record);

    //todo
    List<User> newChatMembers = update.getMessage().getNewChatMembers();
    for (User user : newChatMembers) {
      System.out.println("user = " + user);
    }

    Animation animation = update.getMessage().getAnimation();
    System.out.println("animation = " + animation);

    Document document = update.getMessage().getDocument();
    System.out.println("document = " + document);

    List<MessageEntity> entities = update.getMessage().getEntities();

    if (entities != null) {
      for (MessageEntity messageEntity : entities) {
        System.out.println("messageEntity = " + messageEntity);
      }
    }

    Video video = update.getMessage().getVideo();

    System.out.println("video = " + video);

  }


  public void processChannelPost(Update update, Record record) {

    if (!update.hasChannelPost()) return;

    Message channelPost = update.getChannelPost();
    record.setChannelPost(channelPost.toString());
  }

  public void processChosenInlineQuery(Update update, Record record) {

    if (!update.hasChosenInlineQuery()) return;

    ChosenInlineQuery chosenInlineQuery = update.getChosenInlineQuery();

    record.setChosenInlineQuery(chosenInlineQuery.toString());

  }

  public void processChat(Chat chat, Record record) {
    Long id = chat.getId();
    String title = chat.getTitle();
    String inviteLink = chat.getInviteLink();

    Boolean groupChat = chat.isGroupChat();
    Boolean superGroupChat = chat.isSuperGroupChat();
    Boolean channelChat = chat.isChannelChat();
    Boolean userChat = chat.isUserChat();

    record.setChatId(id.toString());
    record.setTitle(title);
    record.setInviteLink(inviteLink);
    record.setGroupChat(groupChat);
    record.setSuperGroupChat(superGroupChat);
    record.setChannelChat(channelChat);
    record.setUserChat(userChat);
  }

  //todo ich kann ja mit der iphone app irgendeinen kontakt schicken!!!!
  public void processContact(Message message, Record record) {

    if (!message.hasContact()) return;

    Contact contact = message.getContact();
    Integer userID = contact.getUserID();
    String phoneNumber = contact.getPhoneNumber();

    record.setSenderId(userID.toString());
    record.setPhoneNumber(phoneNumber);

  }

  public void processTextMessage(Message message, Record record) {
    if (!message.hasText()) return;

    String text = message.getText();

    record.setMessage(text);

  }

  public void processUser(Message message, Record record) {
    User user = message.getFrom();
    Integer userId = user.getId();
    String firstName = user.getFirstName();
    String lastName = user.getLastName();
    String userName = user.getUserName();
    Boolean isBot = user.getBot();

    record.setSenderId(userId.toString());
    record.setFirstName(firstName);
    record.setLastName(lastName);
    record.setUserName(userName);
    record.setIsBot(isBot);
  }
}