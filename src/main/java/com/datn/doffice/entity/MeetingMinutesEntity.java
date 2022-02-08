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
@Document(collection = "metting_minutes")
public class MeetingMinutesEntity {
	
	@Id
	private String id;
	
	@Field("meeting_id")
	private String meetingId;
	
	@Field("meeting_type")
	private String meetingType;
	
	@Field("goal")
	private String goal;
	
	@Field("agenda")
	private String agenda;
	
	@Field("chairman")
	private String chairman;
	
	@Field("secretary")
	private String secretary;
	
	@Field("next_step")
	private String nextStep;
	
	@Field("is_deleted")
	private Boolean isDeleted;
	
	
}
