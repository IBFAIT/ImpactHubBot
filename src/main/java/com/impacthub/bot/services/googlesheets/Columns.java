package com.impacthub.bot.services.googlesheets;

/**
 * List of DB Columns
 */
public enum Columns {
  MEMBER_ID(0, "Member ID"),
  TELEGRAM_ID(1, "Telegram User ID"),
  FIRST_NAME(2, "First Name"),
  LAST_NAME(3, "Last Name"),
  EMAIL(4, "Email"),
  PHONE(5, "Phone"),
  //todo
  USER_ID(666, "User ID"),
  TELEGRAM_USER_NAME(6, "Telegram-User-Name"),
  IH_MEMBERSHIP(7, "IH-Membership"),
  COMMUNITY(8, "Community"),
  BOT_ADMIN(9, "Bot-Admin"),
  BLOCKED(10, "Blocked");

  private final int colNum;
  private final String colName;

  Columns(int colNum, String colName) {
    this.colName = colName;
    this.colNum = colNum;
  }


    /**
     * Return Column Number from GoogleSpreadSheet
     *
     * @return int Column Number
     */
    public int getColNum() {
        return colNum;
    }


    /**
     * Return Column Name from GoogleSpreadSheet
     *
     * @return String Column Name
     */
    public String getColName() {
        return colName;
    }
}