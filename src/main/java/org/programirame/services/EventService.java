package org.programirame.services;

import org.programirame.models.Event;
import org.programirame.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Service for {@link Event} related actions.
 */
@Service
public class EventService {

    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String HH_MM = "HH:mm";
    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event createEvent(Event event) {
        event.setFromDate(getLocalDateTime(event.getDateFrom(), event.getTimeFrom()));
        event.setToDate(getLocalDateTime(event.getDateTo(), event.getTimeTo()));
        return eventRepository.save(event);
    }

    /**
     * @param date format '2016-02-14'
     * @param time format '18:32'
     * @return
     */
    private LocalDateTime getLocalDateTime(String date, String time) {
        String iso8061 = date + "T" + time + ":00+01:00";
        ZonedDateTime zdt = ZonedDateTime.parse(iso8061);

        return zdt.toLocalDateTime();
    }

    /**
     * Service for retrieving an event.
     *
     * @param eventId The id of the event to retrieve.
     * @return The retrieved event.
     */
    public Event getEvent(Long eventId) {
        Event event = eventRepository.findOne(eventId);

        event.setDateFrom(formatLocalDateTime(event.getFromDate(), YYYY_MM_DD));
        event.setTimeFrom(formatLocalDateTime(event.getFromDate(), HH_MM));

        event.setDateTo(formatLocalDateTime(event.getToDate(), YYYY_MM_DD));
        event.setTimeTo(formatLocalDateTime(event.getToDate(), HH_MM));

        return event;
    }

    /**
     * Formats a {@link LocalDateTime} object into the given format.
     *
     * @param fromDate The {@link LocalDateTime} to format.
     * @param format   The format.
     * @return The formated date and/or time.
     */
    private String formatLocalDateTime(LocalDateTime fromDate, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return fromDate.format(formatter);
    }
}
