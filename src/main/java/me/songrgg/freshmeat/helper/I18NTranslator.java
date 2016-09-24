package me.songrgg.freshmeat.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author songrgg
 * @since 1.0
 */
@Component
public class I18NTranslator {
  @Autowired
  MessageSource messageSource;

  static I18NTranslator translator;

  /**
   * Get message with the specified locale.
   *
   * @param code           message code
   * @param args           args to render the message body
   * @param defaultMessage default message when message code not found
   * @param locale         the i18n locale
   * @return the message body
   */
  public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
    return messageSource.getMessage(code, args, defaultMessage, locale);
  }

  /**
   * Get message with the [simplified chinese] locale.
   *
   * @param code           message code
   * @param args           args to render the message body
   * @param defaultMessage default message when message code not found
   * @return the message body
   */
  public String getMessage(String code, Object[] args, String defaultMessage) {
    return messageSource.getMessage(code, args, defaultMessage, Locale.CHINA);
  }

  /**
   * Get message with the [simplified chinese] locale.
   *
   * @param code message code
   * @param args args to render the message body
   * @return the message body
   */
  public String getMessage(String code, Object[] args) {
    return messageSource.getMessage(code, args, code, Locale.CHINA);
  }

  public static I18NTranslator getInstance() {
    return translator;
  }

  public static void setInstance(I18NTranslator translator) {
    I18NTranslator.translator = translator;
  }
}
