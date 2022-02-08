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
@Document(collection = "work_assign")
public class WorkAssignEntity {

	@Id
	private String id;
	
	@Field("work_id")
	private String workId;
	
	@Field("user_id")
	private String userId;

	
	
	
	
	
	
}
