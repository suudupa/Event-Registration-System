package ca.mcgill.ecse321.eventregistration.model;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "PersonType")
public class Person {
    private String name;

    public void setName(String value) {
        this.name = value;
    }

    @Id
    public String getName() {
        return this.name;
    }
}