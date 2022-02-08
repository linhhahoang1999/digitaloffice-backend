package com.datn.doffice.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
	
	@NotEmpty
	private String workId;
	
	@NotEmpty
	private String title;
	
	
	@NotEmpty
	private String priority;
	
	@NotEmpty
	private String status;
	
	@NotEmpty
	private String description;
	

	@NotEmpty
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private Date beginDate;
	
	@NotEmpty
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private Date endDate;
	

	private List<String> userAssign;

}
