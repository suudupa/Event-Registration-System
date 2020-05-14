package ca.mcgill.ecse321.eventregistration.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.sql.Date;
import java.sql.Time;

@Entity
@DiscriminatorValue("Theater")
public class Theatre extends Event {

    private String title;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public Theatre() {
    }

    public Theatre(String name, Date date, Time startTime, Time endTime, String title) {
        this.setName(name);
        this.setDate(date);
        this.setStartTime(startTime);
        this.setEndTime(endTime);
        this.title = title;
    }
}
