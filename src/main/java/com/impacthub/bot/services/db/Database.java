package com.impacthub.bot.services.db;

import java.util.HashMap;
import java.util.Map;

/**
 * DB Handler Methods
 */
public class Database {
    private final Map<Integer, String> userIdPhoneMap = new HashMap<>();

    /**
     * Insert User Record into DB
     *
     * @param userId      User's ID
     * @param phoneNumber User's phone number
     */
    public void put(int userId, String phoneNumber) {
        userIdPhoneMap.put(userId, phoneNumber);
    }

    /**
     * Returns User's phone number
     *
     * @param userID User's ID
     * @return String User's phone number
     */
    public String getPhoneNumberFromUserId(int userID) {
        return userIdPhoneMap.get(userID);
    }

}
