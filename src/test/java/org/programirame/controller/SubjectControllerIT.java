package org.programirame.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.programirame.models.Subject;
import org.programirame.models.Timetable;
import org.programirame.models.WholeDayEvent;
import org.programirame.models.utility.HourInterval;
import org.programirame.utilities.TestUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.DayOfWeek;

import static junit.framework.TestCase.assertTrue;
import static org.programirame.services.events.TestingConstants.TIME_08_00;
import static org.programirame.services.events.TestingConstants.TIME_17_00;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SubjectControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldCreateNewUser() throws Exception {

        Subject subject = new Subject();

        Timetable timetable = new Timetable();
        timetable.getDailyHours().put(DayOfWeek.MONDAY, new HourInterval(TIME_08_00, TIME_17_00));
        timetable.getDailyHours().put(DayOfWeek.TUESDAY, new HourInterval(TIME_08_00, TIME_17_00));
        timetable.getDailyHours().put(DayOfWeek.WEDNESDAY, new HourInterval(TIME_08_00, TIME_17_00));
        timetable.getDailyHours().put(DayOfWeek.THURSDAY, new HourInterval(TIME_08_00, TIME_17_00));
        timetable.getDailyHours().put(DayOfWeek.FRIDAY, new HourInterval(TIME_08_00, TIME_17_00));

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
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(result.getResponse().getContentAsString());

        assertTrue(rootNode.get("id").asLong() > 0L);
    }

}
