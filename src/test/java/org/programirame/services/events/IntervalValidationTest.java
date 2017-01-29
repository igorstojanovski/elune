package org.programirame.services.events;

import org.programirame.models.*;
import org.programirame.models.utility.HourInterval;
import org.programirame.repositories.EventRepository;
import org.programirame.services.DateTimeService;
import org.programirame.services.EventService;
import org.programirame.services.SubjectService;

import java.time.DayOfWeek;

import static org.mockito.Mockito.mock;

public class IntervalValidationTest {
    protected final String dateFrom;
    protected final String dateTo;
    protected final String timeFrom;
    protected final String timeTo;
    protected EventService eventService;
    protected Subject subject;
    private DateTimeService dateTimeService;

    public IntervalValidationTest(String dateTo, String timeFrom, String dateFrom, String timeTo) {
        this.dateTo = dateTo;
        this.timeFrom = timeFrom;
        this.dateFrom = dateFrom;
        this.timeTo = timeTo;
    }

    protected void before() {
        dateTimeService = new DateTimeService();
        EventRepository eventRepository = mock(EventRepository.class);
        SubjectService subjectService = mock(SubjectService.class);

        eventService = new EventService(eventRepository, dateTimeService, subjectService);

        getTimeTable();

        subject = new Subject();
        subject.setTimetable(getTimeTable());
        subject.getEvents().add(getEvent("2017-02-27", "10:00", "2017-02-27", "12:00"));
    }

    protected Timetable getTimeTable() {
        Timetable timetable = new Timetable();

        timetable.getDailyHours().put(DayOfWeek.MONDAY, new HourInterval("08:00", "17:00"));
        timetable.getDailyHours().put(DayOfWeek.TUESDAY, new HourInterval("08:00", "17:00"));
        timetable.getDailyHours().put(DayOfWeek.WEDNESDAY, new HourInterval("08:00", "17:00"));
        timetable.getDailyHours().put(DayOfWeek.THURSDAY, new HourInterval("08:00", "17:00"));
        timetable.getDailyHours().put(DayOfWeek.FRIDAY, new HourInterval("08:00", "17:00"));

        return timetable;
    }

    protected Event getEvent(String dateFrom, String timeFrom, String dateTo, String timeTo) {
        Event event = new Event();

        Organization organization = new Organization();
        OfferedService offeredService = new OfferedService();

        event.setOrganization(organization);
        event.setOfferedService(offeredService);
        event.setDateFrom(dateFrom);
        event.setTimeFrom(timeFrom);
        event.setDateTo(dateTo);
        event.setTimeTo(timeTo);

        event.setId(1L);

        event.setFromDate(dateTimeService.getLocalDateTime(event.getDateFrom(), event.getTimeFrom()));
        event.setToDate(dateTimeService.getLocalDateTime(event.getDateTo(), event.getTimeTo()));

        return event;
    }
}
