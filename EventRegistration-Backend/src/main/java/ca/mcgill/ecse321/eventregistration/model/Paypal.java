package ca.mcgill.ecse321.eventregistration.model;

import javax.persistence.*;

@Entity
public class Paypal {

	private int id;

	public void setId(int value) {
		this.id = value;
	}

	@Id
	@GeneratedValue
	public int getId() {
		return this.id;
	}

	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	private int amount;

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Paypal() {
	}

	public Paypal(String email, int amount) {
		this.email = email;
		this.amount = amount;
	}

	private Registration registration;

	@OneToOne(mappedBy="paypal")
	public Registration getRegistration() {
		return this.registration;
	}

	public void setRegistration(Registration registration) {
		this.registration = registration;
	}
}
