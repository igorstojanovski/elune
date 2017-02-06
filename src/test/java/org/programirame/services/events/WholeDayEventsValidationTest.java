package org.programirame.services.events;

import org.junit.Before;
import org.junit.Test;
import org.programirame.exceptions.EventOutOfBoundsException;
import org.programirame.models.Event;
import org.programirame.models.WholeDayEvent;
import org.programirame.services.DateTimeService;


public class WholeDayEventsValidationTest {

    private static final String WHOLE_DAY_EVENT_DATE = "2017-02-27";
    private IntervalValidationTestSetup setup;

    @Before
    public void setUp() {
        setup = new IntervalValidationTestSetup(WHOLE_DAY_EVENT_DATE, "08:00", WHOLE_DAY_EVENT_DATE, "09:00");
        setup.before();
    }

    @Test(expected = EventOutOfBoundsException.class)
    public void shouldThrowExceptionIfOverlappingWholeDayEventIsBusy() throws EventOutOfBoundsException {
        Event testEvent = setupSubjectWithWholeDayEvent(true, WHOLE_DAY_EVENT_DATE);
        setup.eventService.validateEventInterval(testEvent, setup.subject);
    }

    @Test
    public void shouldNotThrowExceptionIfOverlappingWholeDayEventIsNotBusy() throws EventOutOfBoundsException {
        Event testEvent = setupSubjectWithWholeDayEvent(false, WHOLE_DAY_EVENT_DATE);
        setup.eventService.validateEventInterval(testEvent, setup.subject);
    }

    private Event setupSubjectWithWholeDayEvent(boolean busy, String wholeDayEventDate) {
        WholeDayEvent wholeDayEvent = new WholeDayEvent();
        DateTimeService dateTimeService = new DateTimeService();
        wholeDayEvent.setLocalDate(dateTimeService.getLocalDate(wholeDayEventDate, "yyyy-MM-dd"));
        wholeDayEvent.setBusy(busy);
        setup.subject.getWholeDayEvents().add(wholeDayEvent);

        return setup.getEvent(setup.dateFrom, setup.timeFrom, setup.dateTo, setup.timeTo);
    }
}
