package org.programirame.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.programirame.models.OfferedService;
import org.programirame.utilities.TestUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OfferedServiceControllerIT {

    @Autowired
    private MockMvc mockMvc;
    private OfferedService offeredServiceRequested;

    @Test
    public void shouldCreateNewUser() throws Exception {

        offeredServiceRequested = new OfferedService();
        offeredServiceRequested.setName("Consultation");
        offeredServiceRequested.setDescription("Consultation with a doctor.");

        MvcResult result = mockMvc.perform(post("/api/service")
                .content(TestUtilities.asJsonString(offeredServiceRequested))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201))
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(result.getResponse().getContentAsString());

        assertTrue(rootNode.get("id").asLong() > 0L);
    }


}
