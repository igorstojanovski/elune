package org.programirame.services;

import org.programirame.models.Event;
import org.programirame.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EventService {

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

    public Event getEvent(Long eventId) {
        Event event = eventRepository.findOne(eventId);

        event.setDateFrom(formatLocalDateTime(event.getFromDate(), "yyyy-MM-dd"));
        event.setTimeFrom(formatLocalDateTime(event.getFromDate(), "HH:mm"));

        event.setDateTo(formatLocalDateTime(event.getToDate(), "yyyy-MM-dd"));
        event.setTimeTo(formatLocalDateTime(event.getToDate(), "HH:mm"));

        return event;
    }

    private String formatLocalDateTime(LocalDateTime fromDate, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return fromDate.format(formatter);
    }
}
