package org.programirame.models;

import org.programirame.models.utility.HourInterval;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Timetable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "DAILY_HOURS")
    @MapKeyColumn(name = "DAY_OF_WEEK")
    @Column(name = "VALUE")
    private Map<DayOfWeek, HourInterval> dailyHours = new HashMap<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<DayOfWeek, HourInterval> getDailyHours() {
        return dailyHours;
    }

    public void setDailyHours(Map<DayOfWeek, HourInterval> dailyHours) {
        this.dailyHours = dailyHours;
    }
}
