package com.impacthub.bot.services.sentinel;

import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDateTime;
import java.util.List;

public class Record {
  private LocalDateTime localDateTime;
  private String chatId;
  private String title;
  private String senderId;
  private String userName;
  private String message;
  private String firstName;
  private String updateId;
  private String channelPost;
  private String chosenInlineQuery;
  private String inviteLink;
  private boolean groupChat;
  private boolean isSuperGroupChat;
  private boolean isChannelChat;
  private boolean isUserChat;
  private String phoneNumber;
  private String lastName;
  private boolean isBot;
  private List<User> newChatMembers;

  public void setIsBot(boolean bot) {
    isBot = bot;
  }

  public LocalDateTime getLocalDateTime() {
    return localDateTime;
  }

  public void setLocalDateTime(LocalDateTime localDateTime) {
    this.localDateTime = localDateTime;
  }

  public String getChatId() {
    return chatId;
  }

  public void setChatId(String chatId) {
    this.chatId = chatId;
  }

  public String getTitle() {
    return title != null ? title : "";
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSenderId() {
    return senderId;
  }

  public void setSenderId(String senderId) {
    this.senderId = senderId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getUpdateId() {
    return updateId;
  }

  public void setUpdateId(String updateId) {
    this.updateId = updateId;
  }

  public String getChannelPost() {
    return channelPost;
  }

  public void setChannelPost(String channelPost) {
    this.channelPost = channelPost;
  }

  public String getChosenInlineQuery() {
    return chosenInlineQuery;
  }

  public void setChosenInlineQuery(String chosenInlineQuery) {
    this.chosenInlineQuery = chosenInlineQuery;
  }

  public String getInviteLink() {
    return inviteLink;
  }

  public void setInviteLink(String inviteLink) {
    this.inviteLink = inviteLink;
  }

  public boolean isGroupChat() {
    return groupChat;
  }

  public void setGroupChat(boolean groupChat) {
    this.groupChat = groupChat;
  }

  public boolean isSuperGroupChat() {
    return isSuperGroupChat;
  }

  public void setSuperGroupChat(boolean superGroupChat) {
    isSuperGroupChat = superGroupChat;
  }

  public boolean isChannelChat() {
    return isChannelChat;
  }

  public void setChannelChat(boolean channelChat) {
    isChannelChat = channelChat;
  }

  public boolean isUserChat() {
    return isUserChat;
  }

  public void setUserChat(boolean userChat) {
    isUserChat = userChat;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public boolean isBot() {
    return isBot;
  }

  public List<User> getNewChatMembers() {
    return newChatMembers;
  }

  public void setNewChatMembers(List<User> newChatMembers) {
    this.newChatMembers = newChatMembers;
  }
}
