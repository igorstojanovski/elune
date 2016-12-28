package org.programirame.services;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DateTimeService {

    /**
     * @param date format '2016-02-14'
     * @param time format '18:32'
     * @return
     */
    public LocalDateTime getLocalDateTime(String date, String time) {
        String iso8061 = date + "T" + time + ":00+01:00";
        ZonedDateTime zdt = ZonedDateTime.parse(iso8061);

        return zdt.toLocalDateTime();
    }

    /**
     * Formats a {@link LocalDateTime} object into the given format.
     *
     * @param fromDate The {@link LocalDateTime} to format.
     * @param format   The format.
     * @return The formated date and/or time.
     */
    public String formatLocalDateTime(LocalDateTime fromDate, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return fromDate.format(formatter);
    }
}
