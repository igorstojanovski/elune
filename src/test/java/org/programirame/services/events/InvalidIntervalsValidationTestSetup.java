package org.programirame.services.events;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.programirame.exceptions.EventOutOfBoundsException;
import org.programirame.models.Event;

import java.util.Arrays;
import java.util.Collection;

import static org.programirame.services.events.TestingConstants.DATE_02_27;
import static org.programirame.services.events.TestingConstants.TIME_09_00;
import static org.programirame.services.events.TestingConstants.TIME_10_30;
import static org.programirame.services.events.TestingConstants.TIME_11_00;
import static org.programirame.services.events.TestingConstants.TIME_13_00;
import static org.programirame.services.events.TestingConstants.TIME_14_00;

@RunWith(Parameterized.class)
public class InvalidIntervalsValidationTestSetup extends IntervalValidationTestSetup {

    public InvalidIntervalsValidationTestSetup(String dateFrom, String timeFrom, String dateTo, String timeTo) {
        super(dateTo, timeFrom, dateFrom, timeTo);
    }

    // "10:00 - 12:00"
    @Parameterized.Parameters
    public static Collection invalidIntervalValues() {
        return Arrays.asList(new String[][]{
                {DATE_02_27, TIME_09_00, DATE_02_27, TIME_11_00},
                {DATE_02_27, TIME_11_00, DATE_02_27, TIME_13_00},
                {DATE_02_27, TIME_09_00, DATE_02_27, TIME_14_00},
                {DATE_02_27, TIME_10_30, DATE_02_27, TIME_10_30}
        });
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
