package org.programirame.controller;


import org.programirame.models.Event;
import org.programirame.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/event")
@ExposesResourceFor(Event.class)
public class EventController {

    private final EntityLinks entityLinks;
    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService, EntityLinks entityLinks) {
        this.eventService = eventService;
        this.entityLinks = entityLinks;
    }

    /**
     * Creates a single event.
     *
     * @param event Data for the event that needs to be created.
     * @return The created event.
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {

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
