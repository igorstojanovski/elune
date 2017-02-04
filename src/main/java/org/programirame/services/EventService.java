package org.programirame.services;

import org.programirame.exceptions.EventOutOfBoundsException;
import org.programirame.exceptions.InvalidDataException;
import org.programirame.models.Event;
import org.programirame.models.Subject;
import org.programirame.models.Timetable;
import org.programirame.models.WholeDayEvent;
import org.programirame.models.utility.HourInterval;
import org.programirame.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;

/**
 * OfferedService for {@link Event} related actions.
 */
@Service
public class EventService {

    private static final String YYYY_MM_DD = "yyyy-MM-dd";
    private static final String HH_MM = "HH:mm";
    private final EventRepository eventRepository;
    private final DateTimeService dateTimeService;
    private final SubjectService subjectService;

    @Autowired
    public EventService(EventRepository eventRepository, DateTimeService dateTimeService,
                        SubjectService subjectService) {
        this.eventRepository = eventRepository;
        this.dateTimeService = dateTimeService;
        this.subjectService = subjectService;
    }

    /**
     * Creates event if the given event meets all validations.
     *
     * @param event The event to create
     * @return The newly created {@link Event}
     * @throws InvalidDataException      If subject id of the given event is non existant
     * @throws EventOutOfBoundsException If the given event is not validated.
     */
    public Event createEvent(Event event) throws InvalidDataException, EventOutOfBoundsException {

        Subject subject = event.getSubject();
        if (subject != null) {
            subject = subjectService.getSubject(subject.getId());
        }

        if (subject == null) throw new InvalidDataException();

        event.setFromDate(
                dateTimeService.getLocalDateTime(event.getDateFrom(), event.getTimeFrom())
        );
        event.setToDate(
                dateTimeService.getLocalDateTime(event.getDateTo(), event.getTimeTo())
        );

        validateEventInterval(event, subject);

        return eventRepository.save(event);
    }

    /**
     * Checks if the event is according to the Subject's timetable
     * and if maybe the event overlaps with already scheduled events.
     *
     * @param event   The {@link Event} that needs to be inserted.
     * @param subject The {@link Subject} that we are scheduling.
     * @throws EventOutOfBoundsException is thrown when any of the conditions are met.
     */
    public void validateEventInterval(Event event, Subject subject) throws EventOutOfBoundsException {

        if (event.getToDate().getDayOfMonth() != event.getFromDate().getDayOfMonth()) {
            throw new EventOutOfBoundsException();
        }

        if (event.getToDate().isBefore(event.getFromDate())) {
            throw new EventOutOfBoundsException();
        }

        if (isEventOutOfSubjectTimeTable(event, subject)) {
            throw new EventOutOfBoundsException();
        }

        validateAgainstScheduledEvents(event, subject);

        validateAgainstWholeDayEvents(event, subject);
    }

    private void validateAgainstWholeDayEvents(Event event, Subject subject) throws EventOutOfBoundsException {
        List<WholeDayEvent> wholeDayEvents = subject.getWholeDayEvents();

        for (WholeDayEvent wholeDayEvent : wholeDayEvents) {
            if (wholeDayEvent.isBusy() && eventOverlapsWithWholeDayEvent(event, wholeDayEvent)) {
                throw new EventOutOfBoundsException();
            }
        }
    }

    private boolean eventOverlapsWithWholeDayEvent(Event event, WholeDayEvent wholeDayEvent) {
        return event.getFromDate().toLocalDate().equals(wholeDayEvent.getLocalDate());
    }

    private void validateAgainstScheduledEvents(Event event, Subject subject) throws EventOutOfBoundsException {
        List<Event> events = subject.getEvents();
        for (Event scheduledEvent : events) {
            if (areEventsOverlapping(event, scheduledEvent)) {
                throw new EventOutOfBoundsException();
            }
        }
    }

    /**
     * Checks if the {@link Event} overlaps in any way with the given referent {@link Event}.
     *
     * @param newEvent      The {@link Event} we need to check.
     * @param referentEvent The referent {@link Event}.
     * @return false if the events do not overlap in any way.
     */
    private boolean areEventsOverlapping(Event newEvent, Event referentEvent) {
        boolean areOverlapping = false;
        if (startOfEventOverlaps(newEvent, referentEvent)) {
            areOverlapping = true;
        }
        if (endOfEventOverlaps(newEvent, referentEvent)) {
            areOverlapping = true;
        }
        if (completelyCovers(newEvent, referentEvent)) {
            areOverlapping = true;
        }
        if (isCompletelyCovered(newEvent, referentEvent)) {
            areOverlapping = true;
        }

        return areOverlapping;
    }

    private boolean isCompletelyCovered(Event newEvent, Event referentEvent) {
        return newEvent.getFromDate().isAfter(referentEvent.getFromDate()) &&
                newEvent.getToDate().isBefore(referentEvent.getToDate());
    }

    private boolean completelyCovers(Event newEvent, Event referentEvent) {
        return newEvent.getFromDate().isBefore(referentEvent.getFromDate()) &&
                newEvent.getToDate().isAfter(referentEvent.getToDate());
    }

    private boolean endOfEventOverlaps(Event newEvent, Event referentEvent) {
        return newEvent.getToDate().isAfter(referentEvent.getFromDate()) &&
                newEvent.getToDate().isBefore(referentEvent.getToDate());
    }

    private boolean startOfEventOverlaps(Event newEvent, Event referentEvent) {
        return newEvent.getFromDate().isAfter(referentEvent.getFromDate()) &&
                newEvent.getFromDate().isBefore(referentEvent.getToDate());
    }

    /**
     * Checks if the event is out of bounds for the given subject. The bounds are set by the TimeTable
     * of the Subject.
     *
     * @param event
     * @param subject
     * @return
     * @throws EventOutOfBoundsException
     */
    private boolean isEventOutOfSubjectTimeTable(Event event, Subject subject) throws EventOutOfBoundsException {
        HourInterval hourInterval = getHourInterval(event, subject);
        LocalDate eventDate = getLocalDate(event.getFromDate());

        return isEventOutOfHourlyBoundsForTheDate(event, hourInterval, eventDate);
    }

    /**
     * Checks if the {@link Event} is in the bounds of the given HourlyInterval for the given
     * date.
     *
     * @param event        The {@link Event} that we need to check.
     * @param hourInterval The hourly referent interval.
     * @param eventDate    The referent date.
     * @return true if the event is out of bounds.
     */
    public boolean isEventOutOfHourlyBoundsForTheDate(Event event, HourInterval hourInterval, LocalDate eventDate) {
        boolean isOutOfBounds = false;
        LocalDateTime intervalStartDateTime
                = combineIntoLocalDateTime(eventDate, LocalTime.parse(hourInterval.getStartTime()));
        LocalDateTime intervalEndDateTime
                = combineIntoLocalDateTime(eventDate, LocalTime.parse(hourInterval.getEndTime()));

        if (intervalStartDateTime.isAfter(event.getFromDate()) ||
                intervalEndDateTime.isBefore(event.getToDate())) {
            isOutOfBounds = true;
        }
        return isOutOfBounds;
    }

    private LocalDateTime combineIntoLocalDateTime(LocalDate eventDate, LocalTime parse) {
        return LocalDateTime.of(eventDate,
                parse);
    }

    private LocalDate getLocalDate(LocalDateTime localDateTime) {
        return LocalDate.of(localDateTime.getYear(), localDateTime.getMonth(),
                localDateTime.getDayOfMonth());
    }

    private HourInterval getHourInterval(Event event, Subject subject) {
        Timetable timeTable = subject.getTimetable();
        return timeTable.getDailyHours().get(event.getFromDate().getDayOfWeek());
    }

    /**
     * OfferedService for retrieving an event.
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
