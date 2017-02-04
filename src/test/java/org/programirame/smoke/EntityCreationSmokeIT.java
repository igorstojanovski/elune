package org.programirame.smoke;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.programirame.models.*;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EntityCreationSmokeIT {

    public static final String NAME = "Igor";
    public static final String SURNAME = "Stojanovski";
    public static final String USER = "igorce";
    public static final String DUMMY_PASS = "dummyPass";
    public static final String API_USER = "/api/user/";
    public static final String CODE_CASTLE = "Code Castle";
    public static final String START_TIME = "08:00";
    public static final String END_TIME = "17:00";
    public static final String API_ORGANIZATION = "/api/organization";
    public static final String WHOLE_DAY_EVENT_DATE = "2017-02-28";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DateTimeService dateTimeService;

    @Test
    public void shouldCreateNewUser() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        User user = new User();
        user.setName(NAME);
        user.setSurname(SURNAME);
        user.setUsername(USER);
        user.setUserType(UserTypes.ADMIN);
        user.setPassword(DUMMY_PASS);

        MvcResult result = postMvcResult(user, API_USER);
        user = mapper.readValue(result.getResponse().getContentAsString(), User.class);

        long userID = user.getId();

        result = getMvcResult(API_USER + userID, 200);
        user = mapper.readValue(result.getResponse().getContentAsString(), User.class);

        Organization organization = new Organization();
        organization.setOwner(user);
        organization.setName(CODE_CASTLE);

        result = postMvcResult(organization, API_ORGANIZATION);
        organization = mapper.readValue(result.getResponse().getContentAsString(), Organization.class);

        Timetable timetable = new Timetable();
        timetable.getDailyHours().put(DayOfWeek.MONDAY, new HourInterval(START_TIME, END_TIME));
        timetable.getDailyHours().put(DayOfWeek.TUESDAY, new HourInterval(START_TIME, END_TIME));
        timetable.getDailyHours().put(DayOfWeek.WEDNESDAY, new HourInterval(START_TIME, END_TIME));
        timetable.getDailyHours().put(DayOfWeek.THURSDAY, new HourInterval(START_TIME, END_TIME));
        timetable.getDailyHours().put(DayOfWeek.FRIDAY, new HourInterval(START_TIME, END_TIME));

        Subject subject = new Subject();
        subject.setTimetable(timetable);
        subject.setOrganization(organization);
        subject.setCreatedBy(user);

        WholeDayEvent wholeDayEvent = new WholeDayEvent();
        wholeDayEvent.setDescription("Day off");
        wholeDayEvent.setBusy(true);
        wholeDayEvent.setDate(WHOLE_DAY_EVENT_DATE);

        subject.getWholeDayEvents().add(wholeDayEvent);

        result = postMvcResult(subject, "/api/subject");

        subject = mapper.readValue(result.getResponse().getContentAsString(), Subject.class);

        Event event = new Event();
        event.setDateFrom("2017-02-27");
        event.setDateTo("2017-02-27");
        event.setTimeFrom("11:00");
        event.setTimeTo("12:00");
        event.setSubject(subject);
        event.setCreatedBy(user);
        event.setOrganization(organization);

        postMvcResult(event, "/api/event");

        result = getMvcResult("/api/subject/" + subject.getId(), 200);
        subject = mapper.readValue(result.getResponse().getContentAsString(), Subject.class);

        assertEquals(subject.getEvents().size(), 1);
        assertEquals(subject.getWholeDayEvents().get(0).getDate(), WHOLE_DAY_EVENT_DATE);
    }

    private MvcResult postMvcResult(Object object, String url) throws Exception {
        return mockMvc.perform(post(url)
                .content(TestUtilities.asJsonString(object))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201))
                .andReturn();
    }

    private MvcResult getMvcResult(String url, int expectedStatus) throws Exception {
        return mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(expectedStatus))
                .andReturn();
    }
}
