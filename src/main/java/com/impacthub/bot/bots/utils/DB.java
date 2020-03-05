package com.impacthub.bot.bots.utils;

import java.util.HashMap;
import java.util.Map;

//todo sollte ein service sein
public class DB {
    private final Map<Integer, String> userIdPhoneMap = new HashMap<>();

    public void put(int userId, String phoneNumber) {
        userIdPhoneMap.put(userId, phoneNumber);
    }

    public String getPhoneNumberFromUserId(int userID) {
        return userIdPhoneMap.get(userID);
    }
}
