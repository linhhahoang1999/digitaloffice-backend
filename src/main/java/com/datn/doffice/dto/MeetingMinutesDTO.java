package com.datn.doffice.dto;

import java.util.Date;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingMinutesDTO {
	
	@NotEmpty
	private String meetingId;
	
	@NotEmpty
	private String meetingType;
	
	@NotEmpty
	private String goal;
	
	@NotEmpty
	private String agenda;
	
	@NotEmpty
	private String chairman;
	
	@NotEmpty
	private String secretary;
	
	@NotEmpty
	private String nextStep;
	
	@NotEmpty
	private Boolean isDeleted;

}
