package ca.mcgill.ecse321.eventregistration.model;

import javax.persistence.*;

@Entity
public class Registration {

	private int id;

	public void setId(int value) {
		this.id = value;
	}

	@Id
	public int getId() {
		return this.id;
	}

	private Person person;

	public Registration() {
		this.paypal = null;
	}

	@ManyToOne(optional = false)
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	private Event event;

	@ManyToOne(optional = false)
	public Event getEvent() {
		return this.event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	private Paypal paypal;

	@OneToOne(cascade=CascadeType.ALL)
	public Paypal getPaypal() {
		return this.paypal;
	}

	public void setPaypal(Paypal paypal) {
		this.paypal = paypal;
	}
}
