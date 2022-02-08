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
@Document(collection = "task_comment")
public class TaskCommentEntity {
	 @Id
	    private String id;

	    @Field("content")
	    private String content;

	    @Field("userId")
	    private String userId;

	    @Field("task_id")
	    private String taskId;

	    @Field("created_at")
	    private Date createdAt;

	    @Field("is_deleted")
	    private Boolean isDeleted;

}
