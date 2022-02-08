package com.datn.doffice.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentDTO {
	
 
    private String id;
    
    private String parentId;


    private String body;


    private String userId;
    
    private String userName;
    

    private String type;

 
    private String entityId;


    private Date createdAt;


}
