package ca.mcgill.ecse321.eventregistration.dao;

import ca.mcgill.ecse321.eventregistration.model.Event;
import ca.mcgill.ecse321.eventregistration.model.Person;
import ca.mcgill.ecse321.eventregistration.model.Registration;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RegistrationRepository extends CrudRepository<Registration, Integer> {

	List<Registration> findByPerson(Person person);

	boolean existsByPersonAndEvent(Person person, Event event);

	Registration findByPersonAndEvent(Person person, Event event);
}
