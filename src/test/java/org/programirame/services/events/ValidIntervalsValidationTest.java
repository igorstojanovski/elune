package org.programirame.services.events;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.programirame.exceptions.EventOutOfBoundsException;
import org.programirame.models.*;

import java.util.Arrays;
import java.util.Collection;

import static org.mockito.Mockito.mock;

@RunWith(Parameterized.class)
public class ValidIntervalsValidationTest extends IntervalValidationTest {

    public ValidIntervalsValidationTest(String dateFrom, String timeFrom, String dateTo, String timeTo) {
        super(dateTo, timeFrom, dateFrom, timeTo);
    }

    // "10:00 - 12:00"
    @Parameterized.Parameters
    public static Collection invalidIntervalValues() {
        return Arrays.asList(new String[][]{
                {"2017-02-27", "08:00", "2017-02-27", "09:00"},
                {"2017-02-27", "12:00", "2017-02-27", "13:00"},
                {"2017-02-27", "14:00", "2017-02-27", "14:30"},
                {"2017-02-27", "12:15", "2017-02-27", "13:20"}});
    }

    @Before
    public void setUp() {

        before();

    }

    @Test
    public void shouldThrowExceptionWhenTheIntervalIsInvalid() throws EventOutOfBoundsException {

        Event testEvent = getEvent(dateFrom, timeFrom, dateTo, timeTo);
        eventService.validateEventInterval(testEvent, subject);
    }

}
