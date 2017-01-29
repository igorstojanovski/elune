package org.programirame.models.utility;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class HourInterval {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String startTime;
    private String endTime;

    public HourInterval(String startTime, String endTime) {
        setStartTime(startTime);
        setEndTime(endTime);
    }

    public HourInterval() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
