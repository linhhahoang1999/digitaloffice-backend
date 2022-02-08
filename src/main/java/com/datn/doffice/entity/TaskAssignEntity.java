package com.datn.doffice.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  Bảng: phân quyền người tham gia tác vụ
 *  
 *      + có thể sửa 		: edit
 *      + có thể bình luận	: comment
 *      
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "task_assign")
public class TaskAssignEntity {
	
	@Id
	private String id;
	
	@Field("task_id")
	private String taskId;
	
	@Field("user_id")
	private String userId;
//
//	@Field("type")
//	private int type;
	

	

}
