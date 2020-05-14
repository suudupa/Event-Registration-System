package ca.mcgill.ecse321.eventregistration.dao;

import ca.mcgill.ecse321.eventregistration.model.Promoter;
import org.springframework.data.repository.CrudRepository;

public interface PromoterRepository extends CrudRepository<Promoter, String> {
    Promoter findByName(String name);
}