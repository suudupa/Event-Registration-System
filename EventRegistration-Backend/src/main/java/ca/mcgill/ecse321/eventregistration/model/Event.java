package ca.mcgill.ecse321.eventregistration.model;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "event")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "TYPE")
public class Event {

    private String name;

    public void setName(String value) {
        this.name = value;
    }

    @Id
    public String getName() {
        return this.name;
    }

    private Date date;

    public void setDate(Date value) {
        this.date = value;
    }

    public Date getDate() {
        return this.date;
    }

    private Time startTime;

    public void setStartTime(Time value) {
        this.startTime = value;
    }

    public Time getStartTime() {
        return this.startTime;
    }

    private Time endTime;

    public void setEndTime(Time value) {
        this.endTime = value;
    }

    public Time getEndTime() {
        return this.endTime;
    }

    private Set<Promoter> promoters = new HashSet<Promoter>();

    @ManyToMany(mappedBy="promotes")
    public Set<Promoter> getPromoters() {
        return this.promoters;
    }

    public void setPromoters(Set<Promoter> promoters) {
        this.promoters = promoters;
    }
}
