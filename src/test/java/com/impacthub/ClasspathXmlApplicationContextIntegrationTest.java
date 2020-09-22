package com.impacthub;

import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ClasspathXmlApplicationContextIntegrationTest {

  @Test
  public void testInternationalization() {
    MessageSource resources = new ClassPathXmlApplicationContext("internationalization.xml");

    String enHello = resources.getMessage("hello", null, "Default", Locale.ENGLISH);
    String enYou = resources.getMessage("you", null, Locale.ENGLISH);
        String enThanks = resources.getMessage("thanks", new Object[]{enYou}, Locale.ENGLISH);


        assertEquals("hello", enHello);
        assertEquals("thank you", enThanks);

        String chHello = resources.getMessage("hello", null, "Default", Locale.SIMPLIFIED_CHINESE);
        String chYou = resources.getMessage("you", null, Locale.SIMPLIFIED_CHINESE);
        String chThanks = resources.getMessage("thanks", new Object[]{chYou}, Locale.SIMPLIFIED_CHINESE);

        assertEquals("你好", chHello);
        assertEquals("谢谢你", chThanks);

    }

}