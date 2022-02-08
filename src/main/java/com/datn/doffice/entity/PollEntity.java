package com.datn.doffice.entity;

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
@Document("poll")
public class PollEntity {

	@Id
	private String id;
	
	@Field("meeting_minutes_id")
	private String meetingMinutesId;
	
	@Field("content")
	private String content;
	
}
