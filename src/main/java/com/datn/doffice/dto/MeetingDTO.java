package com.datn.doffice.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingDTO {
	
	@NotEmpty
	private String title;
	
	@NotEmpty
	private String venue;
		
	@NotEmpty
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Date start;
	
	@NotEmpty
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Date end;
	
	
	@NotEmpty
	private List<String> userAttend;
}
