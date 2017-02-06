package org.programirame.smoke;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.programirame.models.Event;
import org.programirame.models.Organization;
import org.programirame.models.Subject;
import org.programirame.models.Timetable;
import org.programirame.models.User;
import org.programirame.models.UserTypes;
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

import static org.junit.Assert.assertEquals;
import static org.programirame.services.events.TestingConstants.DATE_02_27;
import static org.programirame.services.events.TestingConstants.NAME;
import static org.programirame.services.events.TestingConstants.PASSWORD;
import static org.programirame.services.events.TestingConstants.SURNAME;
import static org.programirame.services.events.TestingConstants.TIME_11_00;
import static org.programirame.services.events.TestingConstants.TIME_12_00;
import static org.programirame.services.events.TestingConstants.USERNAME;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EntityCreationSmokeIT {

    private static final String API_USER = "/api/user/";
    private static final String CODE_CASTLE = "Code Castle";
    private static final String START_TIME = "08:00";
    private static final String END_TIME = "17:00";
    private static final String API_ORGANIZATION = "/api/organization";
    private static final String WHOLE_DAY_EVENT_DATE = "2017-02-28";
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldCreateNewUser() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        User user = new User();
        user.setName(NAME);
        user.setSurname(SURNAME);
        user.setUsername(USERNAME);
        user.setUserType(UserTypes.ADMIN);
        user.setPassword(PASSWORD);

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
        event.setDateFrom(DATE_02_27);
        event.setDateTo(DATE_02_27);
        event.setTimeFrom(TIME_11_00);
        event.setTimeTo(TIME_12_00);
        event.setSubject(subject);
        event.setCreatedBy(user);
        event.setOrganization(organization);

        postMvcResult(event, "/api/event");

        result = getMvcResult("/api/subject/" + subject.getId(), HttpStatus.OK.value());
        subject = mapper.readValue(result.getResponse().getContentAsString(), Subject.class);

        assertEquals(subject.getEvents().size(), 1);
        assertEquals(subject.getWholeDayEvents().get(0).getDate(), WHOLE_DAY_EVENT_DATE);
    }

    private MvcResult postMvcResult(Object object, String url) throws Exception {
        return mockMvc.perform(post(url)
                .content(TestUtilities.asJsonString(object))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.CREATED.value()))
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
