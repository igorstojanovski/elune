package org.programirame.controller;


import org.programirame.exceptions.EventOutOfBoundsException;
import org.programirame.exceptions.InvalidDataException;
import org.programirame.models.Event;
import org.programirame.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/event")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * Creates a single event.
     *
     * @param event Data for the event that needs to be created.
     * @return The created event.
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Event> createEvent(@RequestBody Event event) throws InvalidDataException,
            EventOutOfBoundsException {

        Event result = eventService.createEvent(event);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    /**
     * Retrieves a single {@link Event} by ID.
     *
     * @param eventId The ID of the {@link Event} to be retrieved.
     * @return The retrieved
     */
    @RequestMapping(value = "/{eventId}", method = RequestMethod.GET)
    public ResponseEntity<Event> getEvent(@PathVariable Long eventId) {
        Event event = eventService.getEvent(eventId);

        return new ResponseEntity<>(event, HttpStatus.OK);
    }
}
