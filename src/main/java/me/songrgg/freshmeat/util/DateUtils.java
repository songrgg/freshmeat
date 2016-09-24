package me.songrgg.freshmeat.util;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author songrgg
 * @since 1.0
 */
public class DateUtils {
  /**
   * Get current timestamp.
   */
  public static int nowTime() {
    return (int) (System.currentTimeMillis() / 1000);
  }

  /**
   * Get yesterday's midnight timestamp.
   */
  public static int yesterdayMidnight() {
    LocalTime midnight = LocalTime.MIDNIGHT;
    LocalDate today = LocalDate.now();
    LocalDateTime dateTime = LocalDateTime.of(today.minusDays(1), midnight);
    return (int) (Timestamp.valueOf(dateTime).getTime() / 1000);
  }

  /**
   * Get today's midnight timestamp.
   */
  public static int todayMidnight() {
    LocalTime midnight = LocalTime.MIDNIGHT;
    LocalDate today = LocalDate.now();
    LocalDateTime dateTime = LocalDateTime.of(today, midnight);
    return (int) (Timestamp.valueOf(dateTime).getTime() / 1000);
  }
}
