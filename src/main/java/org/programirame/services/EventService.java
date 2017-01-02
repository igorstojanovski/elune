package org.programirame.services;

import org.programirame.models.Event;
import org.programirame.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for {@link Event} related actions.
 */
@Service
public class EventService {

    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String HH_MM = "HH:mm";
    private final EventRepository eventRepository;
    private final DateTimeService dateTimeService;

    @Autowired
    public EventService(EventRepository eventRepository, DateTimeService dateTimeService) {
        this.eventRepository = eventRepository;
        this.dateTimeService = dateTimeService;
    }

    public Event createEvent(Event event) {
        event.setFromDate(
                dateTimeService.getLocalDateTime(event.getDateFrom(), event.getTimeFrom())
        );
        event.setToDate(
                dateTimeService.getLocalDateTime(event.getDateTo(), event.getTimeTo())
        );
        return eventRepository.save(event);
    }

    /**
     * Service for retrieving an event.
     *
     * @param eventId The id of the event to retrieve.
     * @return The retrieved event.
     */
    public Event getEvent(Long eventId) {
        Event event = eventRepository.findOne(eventId);

        event.setDateFrom(
                dateTimeService.formatLocalDateTime(event.getFromDate(), YYYY_MM_DD)
        );
        event.setTimeFrom(
                dateTimeService.formatLocalDateTime(event.getFromDate(), HH_MM)
        );

        event.setDateTo(
                dateTimeService.formatLocalDateTime(event.getToDate(), YYYY_MM_DD)
        );
        event.setTimeTo(
                dateTimeService.formatLocalDateTime(event.getToDate(), HH_MM)
        );

        return event;
    }
}
