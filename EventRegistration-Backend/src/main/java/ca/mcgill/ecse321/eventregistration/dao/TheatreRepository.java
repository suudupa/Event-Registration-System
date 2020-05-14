package ca.mcgill.ecse321.eventregistration.dao;

import ca.mcgill.ecse321.eventregistration.model.Theatre;
import org.springframework.data.repository.CrudRepository;

public interface TheatreRepository extends CrudRepository<Theatre, String> {
    Theatre findByName(String name);
}