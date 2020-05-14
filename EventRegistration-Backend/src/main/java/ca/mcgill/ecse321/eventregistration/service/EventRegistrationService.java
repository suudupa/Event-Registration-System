package ca.mcgill.ecse321.eventregistration.service;

import ca.mcgill.ecse321.eventregistration.dao.*;
import ca.mcgill.ecse321.eventregistration.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventRegistrationService {

	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private TheatreRepository theatreRepository;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private PromoterRepository promoterRepository;
	@Autowired
	private RegistrationRepository registrationRepository;
	@Autowired
	private PaypalRepository paypalRepository;

	@Transactional
	public Person createPerson(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Person name cannot be empty!");
		} else if (personRepository.existsById(name)) {
			throw new IllegalArgumentException("Person has already been created!");
		}
		Person person = new Person();
		person.setName(name);
		personRepository.save(person);
		return person;
	}

	@Transactional
	public Promoter createPromoter(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Promoter name cannot be empty!");
		} else if (promoterRepository.existsById(name)) {
			throw new IllegalArgumentException("Promoter has already been created!");
		}
		Promoter promoter = new Promoter(name);
		promoterRepository.save(promoter);
		return promoter;
	}

	@Transactional
	public Person getPerson(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Person name cannot be empty!");
		}
		Person person = personRepository.findByName(name);
		return person;
	}

	@Transactional
	public Person getPromoter(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Person name cannot be empty!");
		}
		Promoter promoter = promoterRepository.findByName(name);
		return promoter;
	}

	@Transactional
	public List<Person> getAllPersons() {
		return toList(personRepository.findAll());
	}

	@Transactional
	public List<Promoter> getAllPromoters() {
		return toList(promoterRepository.findAll());
	}

	@Transactional
	public Event buildEvent(Event event, String name, Date date, Time startTime, Time endTime) {
		// Input validation
		String error = "";
		if (name == null || name.trim().length() == 0) {
			error = error + "Event name cannot be empty! ";
		} else if (eventRepository.existsById(name)) {
			throw new IllegalArgumentException("Event has already been created!");
		}
		if (date == null) {
			error = error + "Event date cannot be empty! ";
		}
		if (startTime == null) {
			error = error + "Event start time cannot be empty! ";
		}
		if (endTime == null) {
			error = error + "Event end time cannot be empty! ";
		}
		if (endTime != null && startTime != null && endTime.before(startTime)) {
			error = error + "Event end time cannot be before event start time!";
		}
		error = error.trim();
		if (error.length() > 0) {
			throw new IllegalArgumentException(error);
		}
		event.setName(name);
		event.setDate(date);
		event.setStartTime(startTime);
		event.setEndTime(endTime);
		return event;
	}

	@Transactional
	public Event createEvent(String name, Date date, Time startTime, Time endTime) {
		Event event = new Event();
		buildEvent(event, name, date, startTime, endTime);
		eventRepository.save(event);
		return event;
	}

	@Transactional
	public Theatre createTheatre(String name, Date date, Time startTime, Time endTime, String title) {

		String error = "";

		if (name == null || name.trim().length() == 0) {
			error = error + "Event name cannot be empty! ";
		} else if (theatreRepository.existsById(name)) {
			throw new IllegalArgumentException("Event has already been created!");
		}
		if (date == null) {
			error = error + "Event date cannot be empty! ";
		}
		if (startTime == null) {
			error = error + "Event start time cannot be empty! ";
		}
		if (endTime == null) {
			error = error + "Event end time cannot be empty! ";
		}
		if (endTime != null && startTime != null && endTime.before(startTime)) {
			error = error + "Event end time cannot be before event start time!";
		}
		if (title == null || title.trim().length() == 0) {
			error = error + "Theatre title cannot be empty! ";
		}

		error = error.trim();
		if (error.length() > 0) {
			throw new IllegalArgumentException(error);
		}

		Theatre theatre = new Theatre(name, date, startTime, endTime, title);
		theatreRepository.save(theatre);
		return theatre;
	}

	@Transactional
	public Event getEvent(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Event name cannot be empty!");
		}
		Event event = eventRepository.findByName(name);
		return event;
	}

	// This returns all objects of instance "Event" (Subclasses are filtered out)
	@Transactional
	public List<Event> getAllEvents() {
		return toList(eventRepository.findAll()).stream().filter(e -> e.getClass().equals(Event.class)).collect(Collectors.toList());
	}

	@Transactional
	public List<Theatre> getAllTheatres() {
		return toList(theatreRepository.findAll());
	}

	@Transactional
	public Registration register(Person person, Event event) {
		String error = "";
		if (person == null) {
			error = error + "Person needs to be selected for registration! ";
		} else if (!personRepository.existsById(person.getName())) {
			error = error + "Person does not exist! ";
		}
		if (event == null) {
			error = error + "Event needs to be selected for registration!";
		} else if (!eventRepository.existsById(event.getName())) {
			error = error + "Event does not exist!";
		}
		if (registrationRepository.existsByPersonAndEvent(person, event)) {
			error = error + "Person is already registered to this event!";
		}

		error = error.trim();

		if (error.length() > 0) {
			throw new IllegalArgumentException(error);
		}

		Registration registration = new Registration();
		registration.setId(person.getName().hashCode() * event.getName().hashCode());
		registration.setPerson(person);
		registration.setEvent(event);

		registrationRepository.save(registration);

		return registration;
	}

	@Transactional
	public Paypal createPaypalPay(String accountId, int amount) {

		String error = "";

		if (accountId == null || accountId.trim().length() == 0 || !accountId.matches("\\w+@\\w+(\\.\\w+)+")) {
			error = error + "Email is null or has wrong format!";
		}
		if (amount < 0) {
			error = error + "Payment amount cannot be negative!";
		}

		error = error.trim();
		if (error.length() > 0) {
			throw new IllegalArgumentException(error);
		}

		Paypal paypal = new Paypal(accountId, amount);
		paypalRepository.save(paypal);
		return paypal;
	}

	@Transactional
	public void pay(Registration registration, Paypal paypal) {

		String error = "";

		if (registration == null) {
			error = error + "Registration and payment cannot be null!";
		}
		if (paypal == null) {
			error = error + "Registration and payment cannot be null!";
		}

		error = error.trim();
		if (error.length() > 0) {
			throw new IllegalArgumentException(error);
		}

		registration.setPaypal(paypal);
		registrationRepository.save(registration);
		paypal.setRegistration(registration);
		paypalRepository.save(paypal);
	}

	@Transactional
	public void promotesEvent(Promoter promoter, Event event) {

		String error = "";

		if (promoter == null) {
			error = error + "Promoter needs to be selected for promotes! ";
		} else if (!promoterRepository.existsById(promoter.getName())) {
			error = error + "Promoter does not exist! ";
		}
		if (event == null) {
			error = error + "Event needs to be selected for promotes!";
		} else if (!eventRepository.existsById(event.getName())) {
			error = error + "Event does not exist!";
		}
		if (promoter != null && event != null) {
			if (promoter.getPromotes().contains(event) || event.getPromoters().contains(promoter)) {
				error = error + "Promoter is already promoting this event!";
			}
		}

		error = error.trim();
		if (error.length() > 0) {
			throw new IllegalArgumentException(error);
		}

		promoter.getPromotes().add(event);
		event.getPromoters().add(promoter);
	}

	@Transactional
	public List<Registration> getAllRegistrations() {
		return toList(registrationRepository.findAll());
	}

	@Transactional
	public Registration getRegistrationByPersonAndEvent(Person person, Event event) {
		if (person == null || event == null) {
			throw new IllegalArgumentException("Person or Event cannot be null!");
		}

		return registrationRepository.findByPersonAndEvent(person, event);
	}
	@Transactional
	public List<Registration> getRegistrationsForPerson(Person person){
		if(person == null) {
			throw new IllegalArgumentException("Person cannot be null!");
		}
		return registrationRepository.findByPerson(person);
	}

	@Transactional
	public List<Registration> getRegistrationsByPerson(Person person) {
		return toList(registrationRepository.findByPerson(person));
	}

	@Transactional
	public List<Event> getEventsAttendedByPerson(Person person) {
		if (person == null) {
			throw new IllegalArgumentException("Person cannot be null!");
		}
		List<Event> eventsAttendedByPerson = new ArrayList<>();
		for (Registration r : registrationRepository.findByPerson(person)) {
			eventsAttendedByPerson.add(r.getEvent());
		}
		return eventsAttendedByPerson;
	}

	private <T> List<T> toList(Iterable<T> iterable) {
		List<T> resultList = new ArrayList<T>();
		for (T t : iterable) {
			resultList.add(t);
		}
		return resultList;
	}
}
