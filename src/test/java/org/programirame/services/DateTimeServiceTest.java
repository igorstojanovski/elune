package org.programirame.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class DateTimeServiceTest {

    public static final String CURRENTLY_ONLY_ALLOWED_DATE_FORMAT = "yyyy-MM-dd";
    public static final String CURRENTLY_ONLY_ALLOWED_TIME_FORMAT = "HH:mm";
    private final String date;
    private final String time;
    private DateTimeService dateTimeService;

    public DateTimeServiceTest(String date, String time) {
        this.date = date;
        this.time = time;
    }

    @Parameterized.Parameters
    public static Collection primeNumbers() {
        return Arrays.asList(
                new String[]{"2017-01-01", "00:00"},
                new String[]{"2017-01-02", "23:15"},
                new String[]{"2017-12-01", "00:00"},
                new String[]{"2017-11-11", "00:10"},
                new String[]{"2016-01-01", "22:15"});
    }

    @Before
    public void setUp() throws Exception {
        dateTimeService = new DateTimeService();
    }

    @Test
    public void getLocalDateTime() throws Exception {
        LocalDateTime localDateTime = dateTimeService.getLocalDateTime(date, time);
        String formatedDate = dateTimeService.formatLocalDateTime(localDateTime, CURRENTLY_ONLY_ALLOWED_DATE_FORMAT);
        String formatedTime = dateTimeService.formatLocalDateTime(localDateTime, CURRENTLY_ONLY_ALLOWED_TIME_FORMAT);

        assertEquals(formatedDate, date);
        assertEquals(formatedTime, time);
    }
}