package org.programirame.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.programirame.models.Subject;
import org.programirame.models.Timetable;
import org.programirame.models.WholeDayEvent;
import org.programirame.models.utility.HourInterval;
import org.programirame.services.DateTimeService;
import org.programirame.utilities.TestUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.DayOfWeek;

import static junit.framework.TestCase.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SubjectControllerIT {

    public static final String START_TIME = "08:00";
    public static final String END_TIME = "17:00";
    @Autowired
    DateTimeService dateTimeService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldCreateNewUser() throws Exception {

        Subject subject = new Subject();

        Timetable timetable = new Timetable();
        timetable.getDailyHours().put(DayOfWeek.MONDAY, new HourInterval(START_TIME, END_TIME));
        timetable.getDailyHours().put(DayOfWeek.TUESDAY, new HourInterval(START_TIME, END_TIME));
        timetable.getDailyHours().put(DayOfWeek.WEDNESDAY, new HourInterval(START_TIME, END_TIME));
        timetable.getDailyHours().put(DayOfWeek.THURSDAY, new HourInterval(START_TIME, END_TIME));
        timetable.getDailyHours().put(DayOfWeek.FRIDAY, new HourInterval(START_TIME, END_TIME));

        subject.setTimetable(timetable);

        WholeDayEvent wholeDayEvent = new WholeDayEvent();
        wholeDayEvent.setDescription("Day off");
        wholeDayEvent.setBusy(true);
        wholeDayEvent.setDate("2017-02-27");

        subject.getWholeDayEvents().add(wholeDayEvent);

        MvcResult result = mockMvc.perform(post("/api/subject")
                .content(TestUtilities.asJsonString(subject))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201))
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(result.getResponse().getContentAsString());

        assertTrue(rootNode.get("id").asLong() > 0L);
    }

}
