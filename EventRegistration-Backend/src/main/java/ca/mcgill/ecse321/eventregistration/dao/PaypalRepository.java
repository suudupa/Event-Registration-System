package ca.mcgill.ecse321.eventregistration.dao;

import ca.mcgill.ecse321.eventregistration.model.Paypal;
import org.springframework.data.repository.CrudRepository;

public interface PaypalRepository extends CrudRepository<Paypal, Integer> {
}
