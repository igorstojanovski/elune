package org.programirame.services.events;


import org.junit.Before;
import org.junit.Test;
import org.programirame.exceptions.EventOutOfBoundsException;
import org.programirame.exceptions.InvalidDataException;
import org.programirame.models.Event;
import org.programirame.models.OfferedService;
import org.programirame.models.Organization;
import org.programirame.models.Subject;
import org.programirame.models.Timetable;
import org.programirame.models.utility.HourInterval;
import org.programirame.repositories.EventRepository;
import org.programirame.services.DateTimeService;
import org.programirame.services.EventService;
import org.programirame.services.SubjectService;

import java.time.DayOfWeek;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.programirame.services.events.TestingConstants.DATE_02_27;
import static org.programirame.services.events.TestingConstants.DATE_02_28;
import static org.programirame.services.events.TestingConstants.TIME_07_00;
import static org.programirame.services.events.TestingConstants.TIME_08_00;
import static org.programirame.services.events.TestingConstants.TIME_08_10;
import static org.programirame.services.events.TestingConstants.TIME_09_00;
import static org.programirame.services.events.TestingConstants.TIME_17_00;

public class EventServiceTest {

    private SubjectService subjectService;
    private EventService eventService;
    private Event eventWithId;
    private EventRepository eventRepository;

    @Before
    public void setUp() {

        DateTimeService dateTimeService = new DateTimeService();
        eventRepository = mock(EventRepository.class);
        subjectService = mock(SubjectService.class);

        eventService = new EventService(eventRepository, dateTimeService, subjectService);
        Event eventWithoutId = getEventWithoutId(DATE_02_27, TIME_08_00, DATE_02_27, TIME_09_00);
        eventWithId = getEventWithoutId(DATE_02_27, TIME_07_00, DATE_02_27, TIME_09_00);
        eventWithId.setId(1L);

        setUpSubjectService();

        eventWithId.setFromDate(
                dateTimeService.getLocalDateTime(eventWithoutId.getDateFrom(), eventWithoutId.getTimeFrom())
        );
        eventWithId.setToDate(
                dateTimeService.getLocalDateTime(eventWithoutId.getDateTo(), eventWithoutId.getTimeTo())
        );

        doReturn(eventWithId).when(eventRepository).save(eventWithoutId);
    }

    private void setUpSubjectService() {
        Subject subjectWithId = new Subject();
        subjectWithId.setId(1L);

        doReturn(subjectWithId).when(subjectService).getSubject(1L);
    }

    @Test(expected = EventOutOfBoundsException.class)
    public void shouldThrowAnExceptionIfAfterIsBeforeTo() throws InvalidDataException, EventOutOfBoundsException {
        Event eventOutOfBounds = getEventWithoutId(DATE_02_27, TIME_09_00, DATE_02_27, TIME_08_00);
        eventOutOfBounds.setSubject(getSetupSubject());
        doReturn(getSetupSubject()).when(subjectService).getSubject(1L);
        eventService.createEvent(eventOutOfBounds);
    }

    @Test(expected = EventOutOfBoundsException.class)
    public void shouldThrowAnExceptionIfEventEndsInDifferentDay() throws InvalidDataException,
            EventOutOfBoundsException {
        Event eventOutOfBounds = getEventWithoutId(DATE_02_27, TIME_09_00, DATE_02_28, TIME_08_00);
        eventOutOfBounds.setSubject(getSetupSubject());
        doReturn(getSetupSubject()).when(subjectService).getSubject(1L);
        eventService.createEvent(eventOutOfBounds);
    }

    @Test(expected = InvalidDataException.class)
    public void shouldThrowExceptionIfSubjectDoesNotExist() throws InvalidDataException, EventOutOfBoundsException {
        doThrow(InvalidDataException.class).when(subjectService).getSubject(1L);
        Event createdEvent = eventService.createEvent(eventWithId);

        assertEquals(createdEvent.getId(), eventWithId.getId());
    }

    @Test(expected = EventOutOfBoundsException.class)
    public void shouldNotCreateEventIfOutOfTimeTable() throws InvalidDataException, EventOutOfBoundsException {
        Event eventOutOfBounds = getEventWithoutId(DATE_02_27, TIME_07_00, DATE_02_27, TIME_09_00);
        eventOutOfBounds.setSubject(getSetupSubject());
        doReturn(getSetupSubject()).when(subjectService).getSubject(1L);
        eventService.createEvent(eventOutOfBounds);
    }

    @Test
    public void shouldCreateEventIfInTimeTable() throws InvalidDataException, EventOutOfBoundsException {
        Event eventOutOfBounds = getEventWithoutId(DATE_02_27, TIME_08_10, DATE_02_27, TIME_09_00);
        eventOutOfBounds.setSubject(getSetupSubject());
        doReturn(getSetupSubject()).when(subjectService).getSubject(1L);
        doReturn(eventOutOfBounds).when(eventRepository).save(eventOutOfBounds);

        Event createdEvent = eventService.createEvent(eventOutOfBounds);

        assertNotNull(createdEvent);
    }

    private Event getEventWithoutId(String dateFrom, String timeFrom, String dateTo, String timeTo) {
        Event event = new Event();

        Organization organization = new Organization();
        OfferedService offeredService = new OfferedService();

        event.setOrganization(organization);
        event.setOfferedService(offeredService);
        event.setDateFrom(dateFrom);
        event.setTimeFrom(timeFrom);
        event.setDateTo(dateTo);
        event.setTimeTo(timeTo);

        Subject subject = getSetupSubject();

        event.setSubject(subject);

        return event;
    }

    private Subject getSetupSubject() {
        Subject subject = new Subject();
        subject.setId(1L);

        Timetable timetable = new Timetable();

        timetable.getDailyHours().put(DayOfWeek.MONDAY, new HourInterval(TIME_08_00, TIME_17_00));
        timetable.getDailyHours().put(DayOfWeek.TUESDAY, new HourInterval(TIME_08_00, TIME_17_00));
        timetable.getDailyHours().put(DayOfWeek.WEDNESDAY, new HourInterval(TIME_08_00, TIME_17_00));
        timetable.getDailyHours().put(DayOfWeek.THURSDAY, new HourInterval(TIME_08_00, TIME_17_00));
        timetable.getDailyHours().put(DayOfWeek.FRIDAY, new HourInterval(TIME_08_00, TIME_17_00));

        subject.setTimetable(timetable);

        return subject;
    }
}
