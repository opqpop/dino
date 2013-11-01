package com.lolRiver.river.util;

import org.joda.time.*;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Utilities for manipulating dates.
 */
public class DateUtil {

    private DateUtil() {
        /* this constructor will never be invoked,
         * which enforces non-instantiability
         */
    }

    public static Timestamp addSecondsToTimestamp(int numSeconds, Timestamp timestamp) {
        return dateTimeToTimestamp(timeStampToDateTime(timestamp).plusSeconds(numSeconds));
    }

    public static Timestamp addHoursToTimestamp(int numHours, Timestamp timestamp) {
        return dateTimeToTimestamp(timeStampToDateTime(timestamp).plusHours(numHours));
    }

    public static Timestamp subtractSecondsFromTimestamp(int numSeconds, Timestamp timestamp) {
        return dateTimeToTimestamp(timeStampToDateTime(timestamp).minusSeconds(numSeconds));
    }

    public static Timestamp addDaysToTimestamp(int numDays, Timestamp timestamp) {
        return dateTimeToTimestamp(timeStampToDateTime(timestamp).plusDays(numDays));
    }

    public static Timestamp subtractDaysFromTimestamp(int numDays, Timestamp timestamp) {
        return dateTimeToTimestamp(timeStampToDateTime(timestamp).minusDays(numDays));
    }

    public static Timestamp getCurrentTimestamp() {
        java.util.Date today = new java.util.Date();
        return new Timestamp(today.getTime());
    }

    public static DateTime getCurrentDateTime() {
        return DateTime.now();
    }

    public static Timestamp timestampFromMilliseconds(long milliseconds) {
        Date date = new Date(milliseconds);
        return new Timestamp(date.getTime());
    }

    public static DateTime timeStampToDateTime(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return new DateTime(timestamp.getTime());
    }

    public static Timestamp dateTimeToTimestamp(DateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return new Timestamp(dateTime.getMillis());
    }

    public static boolean daysSinceNowIsLessThan(DateTime dt, int daysSinceNow) {
        return daysBetweenIsLessThan(dt, DateUtil.timeStampToDateTime(DateUtil.getCurrentTimestamp()), daysSinceNow);
    }

    public static boolean daysBetweenIsLessThan(DateTime dt1, DateTime dt2, int daysBetween) {
        return Days.daysBetween(dt1, dt2).isLessThan(Days.days(daysBetween));
    }

    public static int yearsSinceNow(Timestamp timestamp) {
        return yearsSinceNow(timeStampToDateTime(timestamp));
    }

    public static int yearsSinceNow(DateTime dateTime) {
        return yearsBetweenUnorderedDateTimes(dateTime, getCurrentDateTime());
    }

    public static int monthsSinceNow(Timestamp timestamp) {
        return monthsSinceNow(timeStampToDateTime(timestamp));
    }

    public static int monthsSinceNow(DateTime dateTime) {
        return monthsBetweenUnorderedDateTimes(dateTime, getCurrentDateTime());
    }

    public static int weeksSinceNow(Timestamp timestamp) {
        return weeksSinceNow(timeStampToDateTime(timestamp));
    }

    public static int weeksSinceNow(DateTime dateTime) {
        return weeksBetweenUnorderedDateTimes(dateTime, getCurrentDateTime());
    }

    public static int daysSinceNow(Timestamp timestamp) {
        return daysSinceNow(timeStampToDateTime(timestamp));
    }

    public static int daysSinceNow(DateTime dateTime) {
        return daysBetweenUnorderedDateTimes(dateTime, getCurrentDateTime());
    }

    public static int minutesSinceNow(Timestamp timestamp) {
        return minutesSinceNow(timeStampToDateTime(timestamp));
    }

    public static int minutesSinceNow(DateTime dateTime) {
        return minutesBetweenUnorderedDateTimes(dateTime, getCurrentDateTime());
    }

    /**
     * @param timeStamp1 first time stamp
     * @param timeStamp2 second time stamp
     * @return number of years between two timestamps
     */
    public static int yearsBetweenUnorderedTimestamps(Timestamp timeStamp1, Timestamp timeStamp2) {
        return yearsBetweenUnorderedDateTimes(timeStampToDateTime(timeStamp1), timeStampToDateTime(timeStamp2));
    }

    /**
     * @param dateTime1 first date time
     * @param dateTime2 second date time
     * @return number of years between two date times
     */
    public static int yearsBetweenUnorderedDateTimes(DateTime dateTime1, DateTime dateTime2) {
        return Math.abs(Years.yearsBetween(dateTime1, dateTime2).getYears());
    }

    /**
     * @param timeStamp1 first time stamp
     * @param timeStamp2 second time stamp
     * @return number of years timeStamp2 is ahead or behind timeStamp1
     */
    public static int yearsBetweenOrderedTimestamps(Timestamp timeStamp1, Timestamp timeStamp2) {
        return yearsBetweenOrderedDateTimes(timeStampToDateTime(timeStamp1), timeStampToDateTime(timeStamp2));
    }

    /**
     * @param dateTime1 first date time
     * @param dateTime2 second date time
     * @return number of years dateTime2 is ahead or behind dateTime2
     */
    public static int yearsBetweenOrderedDateTimes(DateTime dateTime1, DateTime dateTime2) {
        return Years.yearsBetween(dateTime1, dateTime2).getYears();
    }

    /**
     * @param timeStamp1 first time stamp
     * @param timeStamp2 second time stamp
     * @return number of months between two timestamps
     */
    public static int monthsBetweenUnorderedTimestamps(Timestamp timeStamp1, Timestamp timeStamp2) {
        return monthsBetweenUnorderedDateTimes(timeStampToDateTime(timeStamp1), timeStampToDateTime(timeStamp2));
    }

    /**
     * @param dateTime1 first date time
     * @param dateTime2 second date time
     * @return number of months between two date times
     */
    public static int monthsBetweenUnorderedDateTimes(DateTime dateTime1, DateTime dateTime2) {
        return Math.abs(Months.monthsBetween(dateTime1, dateTime2).getMonths());
    }

    /**
     * @param timeStamp1 first time stamp
     * @param timeStamp2 second time stamp
     * @return number of months timeStamp2 is ahead or behind timeStamp1
     */
    public static int monthsBetweenOrderedTimestamps(Timestamp timeStamp1, Timestamp timeStamp2) {
        return monthsBetweenOrderedDateTimes(timeStampToDateTime(timeStamp1), timeStampToDateTime(timeStamp2));
    }

    /**
     * @param dateTime1 first date time
     * @param dateTime2 second date time
     * @return number of months dateTime2 is ahead or behind dateTime2
     */
    public static int monthsBetweenOrderedDateTimes(DateTime dateTime1, DateTime dateTime2) {
        return Months.monthsBetween(dateTime1, dateTime2).getMonths();
    }

    /**
     * @param timeStamp1 first time stamp
     * @param timeStamp2 second time stamp
     * @return number of weeks between two timestamps
     */
    public static int weeksBetweenUnorderedTimestamps(Timestamp timeStamp1, Timestamp timeStamp2) {
        return weeksBetweenUnorderedDateTimes(timeStampToDateTime(timeStamp1), timeStampToDateTime(timeStamp2));
    }

    /**
     * @param dateTime1 first date time
     * @param dateTime2 second date time
     * @return number of weeks between two date times
     */
    public static int weeksBetweenUnorderedDateTimes(DateTime dateTime1, DateTime dateTime2) {
        return Math.abs(Weeks.weeksBetween(dateTime1, dateTime2).getWeeks());
    }

    /**
     * @param timeStamp1 first time stamp
     * @param timeStamp2 second time stamp
     * @return number of weeks timeStamp2 is ahead or behind timeStamp1
     */
    public static int weeksBetweenOrderedTimestamps(Timestamp timeStamp1, Timestamp timeStamp2) {
        return weeksBetweenOrderedDateTimes(timeStampToDateTime(timeStamp1), timeStampToDateTime(timeStamp2));
    }

    /**
     * @param dateTime1 first date time
     * @param dateTime2 second date time
     * @return number of weeks dateTime2 is ahead or behind dateTime2
     */
    public static int weeksBetweenOrderedDateTimes(DateTime dateTime1, DateTime dateTime2) {
        return Weeks.weeksBetween(dateTime1, dateTime2).getWeeks();
    }

    /**
     * @param timeStamp1 first time stamp
     * @param timeStamp2 second time stamp
     * @return number of days between two timestamps
     */
    public static int daysBetweenUnorderedTimestamps(Timestamp timeStamp1, Timestamp timeStamp2) {
        return daysBetweenUnorderedDateTimes(timeStampToDateTime(timeStamp1), timeStampToDateTime(timeStamp2));
    }

    /**
     * @param dateTime1 first date time
     * @param dateTime2 second date time
     * @return number of days between two date times
     */
    public static int daysBetweenUnorderedDateTimes(DateTime dateTime1, DateTime dateTime2) {
        return Math.abs(Days.daysBetween(dateTime1, dateTime2).getDays());
    }

    /**
     * @param timeStamp1 first time stamp
     * @param timeStamp2 second time stamp
     * @return number of days timeStamp2 is ahead or behind timeStamp1
     */
    public static int daysBetweenOrderedTimestamps(Timestamp timeStamp1, Timestamp timeStamp2) {
        return daysBetweenOrderedDateTimes(timeStampToDateTime(timeStamp1), timeStampToDateTime(timeStamp2));
    }

    /**
     * @param dateTime1 first date time
     * @param dateTime2 second date time
     * @return number of days dateTime2 is ahead or behind dateTime2
     */
    public static int daysBetweenOrderedDateTimes(DateTime dateTime1, DateTime dateTime2) {
        return Days.daysBetween(dateTime1, dateTime2).getDays();
    }

    /**
     * @param timeStamp1 first time stamp
     * @param timeStamp2 second time stamp
     * @return number of minutes between two timestamps
     */
    public static int minutesBetweenUnorderedTimestamps(Timestamp timeStamp1, Timestamp timeStamp2) {
        return minutesBetweenUnorderedDateTimes(timeStampToDateTime(timeStamp1), timeStampToDateTime(timeStamp2));
    }

    /**
     * @param dateTime1 first date time
     * @param dateTime2 second date time
     * @return number of minutes between two date times
     */
    public static int minutesBetweenUnorderedDateTimes(DateTime dateTime1, DateTime dateTime2) {
        return Math.abs(Minutes.minutesBetween(dateTime1, dateTime2).getMinutes());
    }

    /**
     * @param timeStamp1 first time stamp
     * @param timeStamp2 second time stamp
     * @return number of minutes timeStamp2 is ahead or behind timeStamp1
     */
    public static int minutesBetweenOrderedTimestamps(Timestamp timeStamp1, Timestamp timeStamp2) {
        return minutesBetweenOrderedDateTimes(timeStampToDateTime(timeStamp1), timeStampToDateTime(timeStamp2));
    }

    /**
     * @param dateTime1 first date time
     * @param dateTime2 second date time
     * @return number of minutes dateTime2 is ahead or behind dateTime2
     */
    public static int minutesBetweenOrderedDateTimes(DateTime dateTime1, DateTime dateTime2) {
        return Minutes.minutesBetween(dateTime1, dateTime2).getMinutes();
    }

    /**
     * @param timeStamp1 first time stamp
     * @param timeStamp2 second time stamp
     * @return number of seconds between two timestamps
     */
    public static int secondsBetweenUnorderedTimestamps(Timestamp timeStamp1, Timestamp timeStamp2) {
        return secondsBetweenUnorderedDateTimes(timeStampToDateTime(timeStamp1), timeStampToDateTime(timeStamp2));
    }

    /**
     * @param dateTime1 first date time
     * @param dateTime2 second date time
     * @return number of seconds between two date times
     */
    public static int secondsBetweenUnorderedDateTimes(DateTime dateTime1, DateTime dateTime2) {
        return Math.abs(Seconds.secondsBetween(dateTime1, dateTime2).getSeconds());
    }

    /**
     * @param timeStamp1 first time stamp
     * @param timeStamp2 second time stamp
     * @return number of seconds timeStamp2 is ahead or behind timeStamp1
     */
    public static int secondsBetweenOrderedTimestamps(Timestamp timeStamp1, Timestamp timeStamp2) {
        return secondsBetweenOrderedDateTimes(timeStampToDateTime(timeStamp1), timeStampToDateTime(timeStamp2));
    }

    /**
     * @param dateTime1 first date time
     * @param dateTime2 second date time
     * @return number of seconds dateTime2 is ahead or behind dateTime2
     */
    public static int secondsBetweenOrderedDateTimes(DateTime dateTime1, DateTime dateTime2) {
        return Seconds.secondsBetween(dateTime1, dateTime2).getSeconds();
    }

    /**
     * Returns x minutes ago / y hours ago / etc.
     *
     * @return
     */
    public static String timeSinceNowMessage(Timestamp timestamp) {
        String timeSinceNowMessage;
        int minutesSinceNow = minutesSinceNow(timestamp);
        if (minutesSinceNow < 60) {
            int amount = minutesSinceNow;
            String interval = amount > 1 ? "minutes" : "minute";
            timeSinceNowMessage = String.format("%d %s ago", amount, interval);
        } else if (minutesSinceNow < 24 * 60) {
            int amount = minutesSinceNow / 60;
            String interval = amount > 1 ? "hours" : "hour";
            timeSinceNowMessage = String.format("%d %s ago", amount, interval);
        } else if (minutesSinceNow < 7 * 24 * 60) {
            int amount = minutesSinceNow / 60 / 24;
            String interval = amount > 1 ? "days" : "day";
            timeSinceNowMessage = String.format("%d %s ago", amount, interval);
        } else if (minutesSinceNow < 4 * 7 * 24 * 60) {
            int amount = minutesSinceNow / 60 / 24 / 7;
            String interval = amount > 1 ? "weeks" : "week";
            timeSinceNowMessage = String.format("%d %s ago", amount, interval);
        } else if (minutesSinceNow < 12 * 4 * 7 * 24 * 60) {
            int amount = minutesSinceNow / 60 / 24 / 7 / 4;
            String interval = amount > 1 ? "months" : "month";
            timeSinceNowMessage = String.format("%d %s ago", amount, interval);
        } else {
            int amount = minutesSinceNow / 60 / 24 / 7 / 4 / 12;
            String interval = amount > 1 ? "years" : "year";
            timeSinceNowMessage = String.format("%d %s ago", amount, interval);
        }
        return timeSinceNowMessage;
    }
}
