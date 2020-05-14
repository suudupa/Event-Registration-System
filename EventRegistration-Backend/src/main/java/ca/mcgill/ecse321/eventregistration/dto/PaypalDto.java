package ca.mcgill.ecse321.eventregistration.dto;

public class PaypalDto {

	private String email;
	private int amount;
	private RegistrationDto registration;

	public PaypalDto() {
	}

	public PaypalDto(String email, int amount) {
		this.email = email;
		this.amount = amount;
	}

	public String getEmail() {
		return email;
	}

	public int getAmount() {
		return amount;
	}

	public RegistrationDto getRegistration() {
		return registration;
	}

	public void setRegistration(RegistrationDto registration) {
		this.registration = registration;
	}
}
