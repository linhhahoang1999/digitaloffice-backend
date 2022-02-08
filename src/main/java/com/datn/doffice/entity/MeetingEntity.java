package com.datn.doffice.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "meeting")
public class MeetingEntity {
	
	@Id
	private String id;
	
	@Field("title")
	private String title;

	
	@Field("venue")
	private String venue;
		
	@Field("start")
	private Date start;
	
	@Field("end")
	private Date end;
	
	@Field("is_canceled")
	private Boolean isCanceled;
	
	@Field("is_deleted")
	private Boolean isDeleted;
	
	

}
