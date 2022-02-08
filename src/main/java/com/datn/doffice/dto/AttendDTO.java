package com.datn.doffice.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttendDTO {
	private String id;
	
	private String userId;

	private String fullName;

	private String email;

	private String phone;
	
	private Date start;

	private String userName;

	private Boolean isConfirm;

	private Boolean isAttend;
	
	private String meetingId;

}
