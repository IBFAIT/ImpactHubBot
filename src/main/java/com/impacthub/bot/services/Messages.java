package com.impacthub.bot.services;

import org.springframework.context.MessageSource;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Locale;

/**
 * Define Localised Message Constants
 */
public interface Messages {

    MessageSource resources = new ClassPathXmlApplicationContext("internationalization.xml");

    //Locale locale = Locale.ENGLISH;
    Locale locale = Locale.SIMPLIFIED_CHINESE;


    String HELLO = resources.getMessage("hello", null, "Default", locale);
    String YOU = resources.getMessage("you", null, "Default", locale);
    String THANKS = resources.getMessage("thanks", null, "Default", locale);
    String GREETINGS = resources.getMessage("Greetings", null, "Default", locale);
    String DAYS = resources.getMessage("days", null, "Default", locale);
    String WELCOME = resources.getMessage("Welcome", null, "Default", locale);


    // Authenticate Command
    String AUTHENTICATED_MSG = resources.getMessage("authenticated_msg", null, "Default", locale);
    String CANNOT_AUTHENTICATE_MSG = resources.getMessage("cannot_authenticate_msg", null, "Default", locale);
    String JOIN_OPTIONS_MSG = resources.getMessage("authenticate_to_join_msg", null, "Default", locale);
    String CONTACT_BUTTON_MSG = resources.getMessage("share_contact_msg", null, "Default", locale);

    //Hello Command
    String ARGS_RECEIVED_MSG = resources.getMessage("hello_arg_msg", null, "Default", locale);


    //Help Command
    String HELP_MSG = resources.getMessage("help_msg", null, "Default", locale);


    //Join Command
    String GROUP_JOIN_OPTIONS_MSG = resources.getMessage("join_group_select_option_msg", null, "Default", locale);


    //Membership Command
    String MEMBERSHIP_MSG1 = resources.getMessage("membership_status_msg1", null, "Default", locale);
    String MEMBERSHIP_MSG2 = resources.getMessage("membership_status_msg2", null, "Default", locale);


    //Start Command
    String WELCOME_MSG1 = resources.getMessage("welcome_msg1", null, "Default", locale);
    String WELCOME_MSG2 = resources.getMessage("welcome_msg2", null, "Default", locale);


    // Concierge Bot
    String CONTACT_RECEIVED_MSG = resources.getMessage("contact_received_msg", null, "Default", locale);
    String UNAUTHORISED_MSG = resources.getMessage("unauthorised_msg", null, "Default", locale);
    String AUTHORISED_MSG = resources.getMessage("authorised_msg", null, "Default", locale);
    String ECHO_MSG = resources.getMessage("echo_msg", null, "Default", locale);
    String ILLEGAL_ACTION = resources.getMessage("illegal_action", null, "Default", locale);


    //Test Bot
    String REQUEST_CONTACT_MSG = resources.getMessage("request_contact_msg", null, "Default", locale);
    String WAIT_FOR_CONTACT_MSG = resources.getMessage("wait_for_contact_msg", null, "Default", locale);
    String REQUEST_LOCATION_MSG = resources.getMessage("fetch_location_msg", null, "Default", locale);
    String WAIT_FOR_LOCATION_MSG = resources.getMessage("wait_for_location", null, "Default", locale);
    String LOCATION_BUTTON_MSG = resources.getMessage("share_location_button", null, "Default", locale);
    String RECEIVED_DETAILS_MSG = resources.getMessage("user_details_msg", null, "Default", locale);


}
