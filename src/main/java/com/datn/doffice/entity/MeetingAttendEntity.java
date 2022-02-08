package com.datn.doffice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "meeting_attend")
public class MeetingAttendEntity {
	@Id
	private String id;
	
	@Field("meeting_id")
	private String meetingId;
	
	@Field("user_id")
	private String userId;
	
	@Field("is_confirm")
	private Boolean isConfirm;
	
	@Field("is_attend")
	private Boolean isAttend;

	

}
