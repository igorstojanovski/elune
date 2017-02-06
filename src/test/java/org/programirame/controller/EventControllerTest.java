package org.programirame.controller;

import org.junit.Before;
import org.junit.Test;
import org.programirame.exceptions.EventOutOfBoundsException;
import org.programirame.models.Event;
import org.programirame.services.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;


public class EventControllerTest {

    private EventService eventService = mock(EventService.class);
    private EventController eventController;

    @Before
    public void setUp() {

        eventController = new EventController(eventService);

    }

    @Test
    public void shouldCallServiceToCreateEventAndReturnResult()
            throws Exception, EventOutOfBoundsException {
        Event event = new Event();
        Event createdEvent = new Event();
        createdEvent.setId(1L);
        doReturn(createdEvent).when(eventService).createEvent(event);

        ResponseEntity<Event> result = eventController.createEvent(event);

        assertEquals(result.getBody(), createdEvent);
        assertEquals(result.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    public void shouldCallServiceToRetrieveEvent() throws Exception {
        Event createdEvent = new Event();
        createdEvent.setId(1L);
        doReturn(createdEvent).when(eventService).getEvent(1L);

        ResponseEntity<Event> result = eventController.getEvent(1L);

        assertEquals(result.getBody(), createdEvent);
        assertEquals(result.getStatusCode(), HttpStatus.OK);
    }

}