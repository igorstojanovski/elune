package org.programirame.services;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DateTimeService {

    /**
     * @param date format '2016-02-14'
     * @param time format '18:32'
     * @return The newly created {@link LocalDateTime} object.
     */
    public LocalDateTime getLocalDateTime(String date, String time) {
        String iso8061 = date + "T" + time + ":00+01:00";
        ZonedDateTime zdt = ZonedDateTime.parse(iso8061);

        return zdt.toLocalDateTime();
    }

    /**
     * Formats a {@link LocalDateTime} object into the given format.
     *
     * @param localDateTime The {@link LocalDateTime} to format.
     * @param format   The format.
     * @return The formated date and/or time.
     */
    public String formatLocalDateTime(LocalDateTime localDateTime, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return localDateTime.format(formatter);
    }

    /**
     * Formats a {@link LocalDate} object into the given format.
     *
     * @param localDate The {@link LocalDate} to format.
     * @param format    The format.
     * @return The formated date and/or time.
     */
    public String formatLocalDate(LocalDate localDate, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return localDate.format(formatter);
    }

    /**
     * Creates {@link LocalDate} object from a String with the given format.
     *
     * @param date   The string representation of the date.
     * @param format The format of the date representation.
     * @return the created {@link LocalDate}
     */
    public LocalDate getLocalDate(String date, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDate.parse(date, formatter);
    }
}
