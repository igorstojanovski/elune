package org.programirame.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.programirame.models.Subject;
import org.programirame.utilities.TestUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static junit.framework.TestCase.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SubjectControllerIT {

    @Autowired
    private MockMvc mockMvc;
    private Subject subject;

    @Test
    public void shouldCreateNewUser() throws Exception {

        subject = new Subject();

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
