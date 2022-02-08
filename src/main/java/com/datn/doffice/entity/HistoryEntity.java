package com.datn.doffice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "history")
public class HistoryEntity {
	@Id
	private String id;

	@Field("user_id")
	private String userId;
	
	@Field("user_name")
	private String userName;
	
	@Field("type")
	private String type;

	@Field("action")
	private String action;

	@Field("description")
	private String description;

	@Field("entity_id")
	private String entityId;

	@Field("created_at")
	private Date createdAt;

}
