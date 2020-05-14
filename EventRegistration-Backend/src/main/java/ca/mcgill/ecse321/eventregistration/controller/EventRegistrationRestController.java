package ca.mcgill.ecse321.eventregistration.controller;

import ca.mcgill.ecse321.eventregistration.dto.*;
import ca.mcgill.ecse321.eventregistration.model.*;
import ca.mcgill.ecse321.eventregistration.service.EventRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class EventRegistrationRestController {

	@Autowired
	private EventRegistrationService service;

	// POST Mappings

	// @formatter:off
	// Turning off formatter here to ease comprehension of the sample code by
	// keeping the linebreaks
	// Example REST call:
	// http://localhost:8088/persons/John
	@PostMapping(value = { "/persons/{name}", "/persons/{name}/" })
	public PersonDto createPerson(@PathVariable("name") String name) throws IllegalArgumentException {
		// @formatter:on
		Person person = service.createPerson(name);
		return convertToDto(person);
	}

	@PostMapping(value = { "/promoters/{name}", "/promoters/{name}/" })
	public PromoterDto createPromoter(@PathVariable("name") String name) throws IllegalArgumentException {
		Promoter promoter = service.createPromoter(name);
		return convertToDto(promoter);
	}

	// @formatter:off
	// Example REST call:
	// http://localhost:8080/events/testevent?date=2013-10-23&startTime=00:00&endTime=23:59
	@PostMapping(value = { "/events/{name}", "/events/{name}/" })
	public EventDto createEvent(@PathVariable("name") String name, @RequestParam Date date,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime startTime,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime endTime)
			throws IllegalArgumentException {
		// @formatter:on
		Event event = service.createEvent(name, date, Time.valueOf(startTime), Time.valueOf(endTime));
		return convertToDto(event);
	}

	@PostMapping(value = { "/theatres/{name}", "/theatres/{name}/" })
	public TheatreDto createTheatre(@PathVariable("name") String name,
									@RequestParam Date date,
									@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime startTime,
									@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime endTime,
									@RequestParam (name = "title") String title)
			throws IllegalArgumentException {

		Theatre theatre = service.createTheatre(name, date, Time.valueOf(startTime), Time.valueOf(endTime), title);
		return convertToDto(theatre);
	}

	// @formatter:off
	@PostMapping(value = { "/register", "/register/" })
	public RegistrationDto registerPersonForEvent(@RequestParam(name = "person") PersonDto pDto,
			@RequestParam(name = "event") EventDto eDto) throws IllegalArgumentException {
		// @formatter:on

		// Both the person and the event are identified by their names
		Person p = service.getPerson(pDto.getName());
		Event e = service.getEvent(eDto.getName());

		Registration r = service.register(p, e);
		return convertToDto(r, p, e);
	}

	@PostMapping(value = { "/assign", "/assign/" })
	public EventDto assignPromoterForEvent(@RequestParam(name = "promoter") PromoterDto pDto,
									   @RequestParam(name = "event") EventDto eDto) throws IllegalArgumentException {
		Promoter p = (Promoter) service.getPromoter(pDto.getName());
		Event e = service.getEvent(eDto.getName());
		service.promotesEvent(p, e);
		return convertToDto(e);
	}

	@PostMapping(value = { "/pay", "/pay/" })
	public PaypalDto personPayForEvent(@RequestParam(name = "person") PersonDto pDto,
								  @RequestParam(name = "event") EventDto eDto,
								  @RequestParam (name = "userID") String email,
								  @RequestParam (name = "amount") int amount) throws IllegalArgumentException {

		Person p = service.getPerson(pDto.getName());
		Event e = service.getEvent(eDto.getName());
		Registration r = service.getRegistrationByPersonAndEvent(p, e);
		Paypal payment = service.createPaypalPay(email, amount);
		service.pay(r, payment);
		return convertToDto(payment);
	}

	// GET Mappings

	@GetMapping(value = { "/events", "/events/" })
	public List<EventDto> getAllEvents() {
		List<EventDto> eventDtos = new ArrayList<>();
		for (Event event : service.getAllEvents()) {
			eventDtos.add(convertToDto(event));
		}
		return eventDtos;
	}

	@GetMapping(value = { "/registrations", "/registrations/" })
	public List<RegistrationDto> getAllRegistrations() {
		List<RegistrationDto> registrationDtos = new ArrayList<>();
		for (Registration registration : service.getAllRegistrations()) {
			registrationDtos.add(convertToDto(registration));
		}
		return registrationDtos;
	}

	// Example REST call:
	// http://localhost:8088/events/person/JohnDoe
	@GetMapping(value = { "/events/person/{name}", "/events/person/{name}/" })
	public List<EventDto> getEventsOfPerson(@PathVariable("name") PersonDto pDto) {
		Person p = convertToDomainObject(pDto);
		return createAttendedEventDtosForPerson(p);
	}

	@GetMapping(value = { "/persons/{name}", "/persons/{name}/" })
	public PersonDto getPersonByName(@PathVariable("name") String name) throws IllegalArgumentException {
		return convertToDto(service.getPerson(name));
	}

	@GetMapping(value = { "/registration", "/registration/" })
	public RegistrationDto getRegistration(@RequestParam(name = "person") PersonDto pDto,
			@RequestParam(name = "event") EventDto eDto) throws IllegalArgumentException {
		// Both the person and the event are identified by their names
		Person p = service.getPerson(pDto.getName());
		Event e = service.getEvent(eDto.getName());

		Registration r = service.getRegistrationByPersonAndEvent(p, e);
		return convertToDtoWithoutPerson(r);
	}

	@GetMapping(value = { "/registrations/person/{name}", "/registrations/person/{name}/" })
	public List<RegistrationDto> getRegistrationsForPerson(@PathVariable("name") PersonDto pDto)
			throws IllegalArgumentException {
		// Both the person and the event are identified by their names
		Person p = service.getPerson(pDto.getName());

		return createRegistrationDtosForPerson(p);
	}

	@GetMapping(value = { "/registrations/{personName}/{eventName}", "/registrations/{personName}/{eventName}/" })
	public PaypalDto getPaypalForRegistration(@PathVariable("personName") PersonDto pDto, @PathVariable("eventName") EventDto eDto)
			throws IllegalArgumentException {

		Person p = service.getPerson(pDto.getName());
		Event e = service.getEvent(eDto.getName());
		Registration r = service.getRegistrationByPersonAndEvent(p, e);
		Paypal paypal = r.getPaypal();
		return convertToDto(paypal);
	}

	@GetMapping(value = { "/persons", "/persons/" })
	public List<PersonDto> getAllPersons() {
		List<PersonDto> persons = new ArrayList<>();
		for (Person person : service.getAllPersons()) {
			persons.add(convertToDto(person));
		}
		return persons;
	}

	@GetMapping(value = { "/events/{name}", "/events/{name}/" })
	public EventDto getEventByName(@PathVariable("name") String name) throws IllegalArgumentException {
		return convertToDto(service.getEvent(name));
	}

	// Model - DTO conversion methods (not part of the API)

	private EventDto convertToDto(Event e) {
		if (e == null) {
			throw new IllegalArgumentException("There is no such Event!");
		}
		EventDto eventDto = new EventDto(e.getName(), e.getDate(), e.getStartTime(), e.getEndTime());
		return eventDto;
	}

	private TheatreDto convertToDto(Theatre t) {
		if (t == null) {
			throw new IllegalArgumentException("There is no such Theatre!");
		}
		TheatreDto theatreDto = new TheatreDto(t.getName(), t.getDate(), t.getStartTime(), t.getEndTime(), t.getTitle());
		return theatreDto;
	}

	private PersonDto convertToDto(Person p) {
		if (p == null) {
			throw new IllegalArgumentException("There is no such Person!");
		}
		PersonDto personDto = new PersonDto(p.getName());
		personDto.setEventsAttended(createAttendedEventDtosForPerson(p));
		return personDto;
	}

	private PromoterDto convertToDto(Promoter p) {
		if (p == null) {
			throw new IllegalArgumentException("There is no such Promoter!");
		}
		PromoterDto promoterDto = new PromoterDto(p.getName(), createAttendedEventDtosForPerson(p));
		return promoterDto;
	}

	private PaypalDto convertToDto(Paypal p) {
		PaypalDto paypalDto = new PaypalDto(p.getEmail(), p.getAmount());
		return paypalDto;
	}

	// DTOs for registrations
	private RegistrationDto convertToDto(Registration r, Person p, Event e) {
		EventDto eDto = convertToDto(e);
		PersonDto pDto = convertToDto(p);
		return new RegistrationDto(pDto, eDto);
	}

	private RegistrationDto convertToDto(Registration r) {
		EventDto eDto = convertToDto(r.getEvent());
		PersonDto pDto = convertToDto(r.getPerson());
		RegistrationDto rDto = new RegistrationDto(pDto, eDto);
		return rDto;
	}

	// return registration dto without person object so that we are not repeating
	// data
	private RegistrationDto convertToDtoWithoutPerson(Registration r) {
		RegistrationDto rDto = convertToDto(r);
		rDto.setPerson(null);
		return rDto;
	}

	private Person convertToDomainObject(PersonDto pDto) {
		List<Person> allPersons = service.getAllPersons();
		for (Person person : allPersons) {
			if (person.getName().equals(pDto.getName())) {
				return person;
			}
		}
		return null;
	}

	// Other extracted methods (not part of the API)

	private List<EventDto> createAttendedEventDtosForPerson(Person p) {
		List<Event> eventsForPerson = service.getEventsAttendedByPerson(p);
		List<EventDto> events = new ArrayList<>();
		for (Event event : eventsForPerson) {
			events.add(convertToDto(event));
		}
		return events;
	}

	private List<RegistrationDto> createRegistrationDtosForPerson(Person p) {
		List<Registration> registrationsForPerson = service.getRegistrationsForPerson(p);
		List<RegistrationDto> registrations = new ArrayList<RegistrationDto>();
		for (Registration r : registrationsForPerson) {
			registrations.add(convertToDtoWithoutPerson(r));
		}
		return registrations;
	}
}