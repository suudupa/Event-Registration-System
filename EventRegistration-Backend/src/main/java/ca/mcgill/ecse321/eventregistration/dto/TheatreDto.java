package ca.mcgill.ecse321.eventregistration.dto;

import java.sql.Date;
import java.sql.Time;

public class TheatreDto extends EventDto {

	private String title;

	public TheatreDto() {
	}

	public TheatreDto(String name, Date date, Time startTime, Time endTime, String title) {
		this.setName(name);
		this.setDate(date);
		this.setStartTime(startTime);
		this.setEndTime(endTime);
		this.title = title;
	}

	public String getTitle() { return title; }
}