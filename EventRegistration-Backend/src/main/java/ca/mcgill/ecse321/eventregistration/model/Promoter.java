package ca.mcgill.ecse321.eventregistration.model;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("Promoter")
public class Promoter extends Person {

    private Set<Event> promotes;

    @ManyToMany(cascade=CascadeType.ALL)
    public Set<Event> getPromotes() {
        return this.promotes;
    }

    public void setPromotes(Set<Event> events) {
        this.promotes = events;
    }

    public Promoter() {
        this.promotes = new HashSet<Event>();
    }

    public Promoter(String name) {
        this.setName(name);
        this.promotes = new HashSet<Event>();
    }
}