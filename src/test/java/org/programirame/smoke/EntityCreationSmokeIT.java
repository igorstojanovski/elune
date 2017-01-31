package org.programirame.smoke;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.programirame.models.*;
import org.programirame.models.utility.HourInterval;
import org.programirame.utilities.TestUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.DayOfWeek;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EntityCreationSmokeIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldCreateNewUser() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        User user = new User();
        user.setName("Igor");
        user.setSurname("Stojanovski");
        user.setUserType(UserTypes.SUBSCRIBER);

        MvcResult result = postMvcResult(user, "/api/user");
        user = mapper.readValue(result.getResponse().getContentAsString(), User.class);


        long userID = user.getId();

        result = getMvcResult("/api/user/" + userID, 200);
        user = mapper.readValue(result.getResponse().getContentAsString(), User.class);

        Organization organization = new Organization();
        organization.setOwner(user);
        organization.setName("Code Castle");

        result = postMvcResult(organization, "/api/organization");
        organization = mapper.readValue(result.getResponse().getContentAsString(), Organization.class);

        Subject subject = new Subject();

        Timetable timetable = new Timetable();
        timetable.getDailyHours().put(DayOfWeek.MONDAY, new HourInterval("08:00", "17:00"));
        timetable.getDailyHours().put(DayOfWeek.TUESDAY, new HourInterval("08:00", "17:00"));
        timetable.getDailyHours().put(DayOfWeek.WEDNESDAY, new HourInterval("08:00", "17:00"));
        timetable.getDailyHours().put(DayOfWeek.THURSDAY, new HourInterval("08:00", "17:00"));
        timetable.getDailyHours().put(DayOfWeek.FRIDAY, new HourInterval("08:00", "17:00"));

        subject.setTimetable(timetable);
        subject.setOrganization(organization);
        subject.setCreatedBy(user);

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

        System.out.println(subject.getEvents().size());
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
