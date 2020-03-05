package com.impacthub.bot.services.googlesheets;

public enum Columns {
    MEMBER_ID(0, "Member ID"),
    FIRST_NAME(1, "First Name"),
    LAST_NAME(2, "Last Name"),
    EMAIL(3, "Email"),
    PHONE(4, "Phone"),
    //todo
    USER_ID(666, "User ID"),
    TELEGRAM_USER_NAME(5, "Telegram-User-Name"),
    IH_MEMBERSHIP(6, "IH-Membership"),
    COMMUNITY(7, "Community"),
    BOT_ADMIN(8, "Bot-Admin"),
    BLOCKED(9, "Blocked");

    private final int colNum;
    private final String colName;

    Columns(int colNum, String colName) {
        this.colName = colName;
        this.colNum = colNum;
    }

    public int getColNum() {
        return colNum;
    }

    public String getColName() {
        return colName;
    }
}