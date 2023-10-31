package com.github.audomsak.poc.promptpay.reconcile.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.github.audomsak.poc.promptpay.reconcile.Constant.INPUT_DATE_FORMAT;
import static com.github.audomsak.poc.promptpay.reconcile.Constant.INPUT_TIME_FORMAT;
import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * A utility class with useful methods for working with date and time.
 */
public class DateTimeUtils {
    // Private constructor to prevent instantiation
    private DateTimeUtils() {
    }

    /**
     * Parse the given date string to {@link LocalDate} object.
     *
     * @param date Date string in this format: {@link com.github.audomsak.poc.promptpay.reconcile.Constant#INPUT_DATE_FORMAT}
     * @return {@link LocalDate} object represents the given date string.
     */
    public static LocalDate toLocalDate(String date) {
        return LocalDate.parse(date, ofPattern(INPUT_DATE_FORMAT));
    }

    /**
     * Parse the given time string to {@link LocalTime} object.
     *
     * @param time Time string in this format: {@link com.github.audomsak.poc.promptpay.reconcile.Constant#INPUT_TIME_FORMAT}
     * @return {@link LocalTime} object represents the given date string.
     */
    public static LocalTime toLocalTime(String time) {
        return LocalTime.parse(time, ofPattern(INPUT_TIME_FORMAT));
    }

    /**
     * Format the given {@link LocalDate} object to String in this format: {@link com.github.audomsak.poc.promptpay.reconcile.Constant#INPUT_DATE_FORMAT}
     *
     * @param date {@link LocalDate} object to be formatted.
     * @return String represents the given {@link LocalDate} object.
     */
    public static String format(LocalDate date) {
        return date.format(ofPattern(INPUT_DATE_FORMAT));
    }

    /**
     * Format the given {@link LocalTime} object to String in this format: {@link com.github.audomsak.poc.promptpay.reconcile.Constant#INPUT_TIME_FORMAT}
     *
     * @param time {@link LocalTime} object to be formatted.
     * @return String represents the given {@link LocalTime} object.
     */
    public static String format(LocalTime time) {
        return time.format(ofPattern(INPUT_TIME_FORMAT));
    }

    /**
     * Convert the given date and time strings to {@link LocalDateTime} object.
     *
     * @param date Date string in this format: {@link com.github.audomsak.poc.promptpay.reconcile.Constant#INPUT_DATE_FORMAT}
     * @param time Time string in this format: {@link com.github.audomsak.poc.promptpay.reconcile.Constant#INPUT_TIME_FORMAT}
     * @return {@link LocalDateTime} object represents the given date and time strings.
     */
    public static LocalDateTime toLocalDateTime(String date, String time) {
        return toLocalDate(date).atTime(toLocalTime(time));
    }

    /**
     * Convert the given {@link LocalDate} and {@link LocalDateTime} objects to {@link LocalDateTime} object.
     *
     * @param date {@link LocalDate} object to be converted.
     * @param time {@link LocalTime} object to be converted.
     * @return {@link LocalDateTime} object represents the given date and time objects.
     */
    public static LocalDateTime toLocalDateTime(LocalDate date, LocalTime time) {
        return date.atTime(time);
    }
}
