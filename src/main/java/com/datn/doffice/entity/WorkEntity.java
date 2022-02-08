package com.datn.doffice.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
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
@Document(collection = "work")
public class WorkEntity {

	@Id
	private String id;
	

	@Field("title")
	private String title;

	@Field("description")
	private String description;

	@Field("begin_date")
	private Date beginDate;

	@Field("end_date")
	private Date endDate;

	@Field("created_at")
	private Date createdAt;

	@Field("created_by")
	private String createdBy;

	@Field("last_edited")
	private Date lastEdited;

	@Field("last_edited_by")
	private String lastEditedBy;

	@Field("is_deleted")
	private Boolean isDeleted;
	
	@Field("is_completed")
	private Boolean isCompleted;
		
	@Field("is_stored")
	private Boolean isStored;

}
