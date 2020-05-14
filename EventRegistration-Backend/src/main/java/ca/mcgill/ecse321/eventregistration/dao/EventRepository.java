package ca.mcgill.ecse321.eventregistration.dao;

import ca.mcgill.ecse321.eventregistration.model.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event, String> {
    Event findByName(String name);
}