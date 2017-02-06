package org.programirame.services.events;

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

import static org.mockito.Mockito.mock;

public class IntervalValidationTestSetup {
    private static final String TIME_08_00 = "08:00";
    private static final String TIME_17_00 = "17:00";
    private static final String DATE_02_27 = "2017-02-27";
    private static final String TIME_12_00 = "12:00";
    private static final String TIME_10_00 = "10:00";
    protected final String dateFrom;
    protected final String dateTo;
    protected final String timeFrom;
    protected final String timeTo;
    protected EventService eventService;
    protected Subject subject;
    private DateTimeService dateTimeService;


    public IntervalValidationTestSetup(String dateTo, String timeFrom, String dateFrom, String timeTo) {
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
        subject.getEvents().add(getEvent(DATE_02_27, TIME_10_00, DATE_02_27, TIME_12_00));
    }

    protected Timetable getTimeTable() {
        Timetable timetable = new Timetable();

        timetable.getDailyHours().put(DayOfWeek.MONDAY, new HourInterval(TIME_08_00, TIME_17_00));
        timetable.getDailyHours().put(DayOfWeek.TUESDAY, new HourInterval(TIME_08_00, TIME_17_00));
        timetable.getDailyHours().put(DayOfWeek.WEDNESDAY, new HourInterval(TIME_08_00, TIME_17_00));
        timetable.getDailyHours().put(DayOfWeek.THURSDAY, new HourInterval(TIME_08_00, TIME_17_00));
        timetable.getDailyHours().put(DayOfWeek.FRIDAY, new HourInterval(TIME_08_00, TIME_17_00));

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
