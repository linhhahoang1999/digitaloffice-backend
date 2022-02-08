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
@Document(collection = "work_comment")
public class WorkCommentEntity {
    @Id
    private String id;
    
    @Field("parent_id")
    private String parentId;


    @Field("body")
    private String body;

    @Field("user_id")
    private String userId;
    
    @Field("type")
    private String type;

    @Field("entity_id")
    private String entityId;

    @Field("created_at")
    private Date createdAt;

    @Field("is_deleted")
    private Boolean isDeleted;
    
}
