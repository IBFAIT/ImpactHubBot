package com.impacthub.bot.services.db;

import java.util.HashMap;
import java.util.Map;

public class Database {
    private final Map<Integer, String> userIdPhoneMap = new HashMap<>();

    public void put(int userId, String phoneNumber) {
        userIdPhoneMap.put(userId, phoneNumber);
    }

    public String getPhoneNumberFromUserId(int userID) {
        return userIdPhoneMap.get(userID);
    }

}
