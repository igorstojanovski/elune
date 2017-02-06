package org.programirame.models;

import org.programirame.models.utility.HourInterval;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Timetable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyColumn(name = "daily_hours")
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
