package org.programirame.services.events;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.programirame.exceptions.EventOutOfBoundsException;
import org.programirame.models.Event;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class InvalidIntervalsValidationTest extends IntervalValidationTest {

    public InvalidIntervalsValidationTest(String dateFrom, String timeFrom, String dateTo, String timeTo) {
        super(dateTo, timeFrom, dateFrom, timeTo);
    }

    // "10:00 - 12:00"
    @Parameterized.Parameters
    public static Collection invalidIntervalValues() {
        return Arrays.asList({"2017-02-27", "09:00", "2017-02-27", "11:00"},
                {"2017-02-27", "11:00", "2017-02-27", "13:00"},
                {"2017-02-27", "09:00", "2017-02-27", "14:00"},
                {"2017-02-27", "10:30", "2017-02-27", "11:30"});
    }

    @Before
    public void setUp() {
        before();
    }

    @Test(expected = EventOutOfBoundsException.class)
    public void shouldThrowExceptionWhenTheIntervalIsInvalid() throws EventOutOfBoundsException {

        Event testEvent = getEvent(dateFrom, timeFrom, dateTo, timeTo);
        eventService.validateEventInterval(testEvent, subject);
    }

}
