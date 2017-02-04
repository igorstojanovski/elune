package org.programirame.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.geo.Distance;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    private Timetable timetable;

    @OneToMany(mappedBy = "subject")
    private List<Event> events = new ArrayList<>();

    @OneToOne
    @JsonIgnore
    private Organization organization;

    @OneToOne
    private User createdBy;

    @OneToMany(cascade = CascadeType.ALL)
    private List<WholeDayEvent> wholeDayEvents = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Timetable getTimetable() {
        return timetable;
    }

    public void setTimetable(Timetable timetable) {
        this.timetable = timetable;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public List<WholeDayEvent> getWholeDayEvents() {
        return wholeDayEvents;
    }

    public void setWholeDayEvents(List<WholeDayEvent> wholeDayEvents) {
        this.wholeDayEvents = wholeDayEvents;
    }
}
