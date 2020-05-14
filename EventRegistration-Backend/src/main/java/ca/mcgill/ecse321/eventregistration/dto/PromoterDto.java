package ca.mcgill.ecse321.eventregistration.dto;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("unchecked")
public class PromoterDto extends PersonDto {

	public PromoterDto() {
		this.setEventsAttended(Collections.EMPTY_LIST);
	}

	public PromoterDto(String name, List<EventDto> events) {
		this.setName(name);
		this.setEventsAttended(events);
	}
}
