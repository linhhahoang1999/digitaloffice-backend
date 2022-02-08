package com.datn.doffice.service;

import java.util.List;

import com.datn.doffice.dto.UserLoginDetailDTO;
import com.datn.doffice.dto.AttendDTO;
import com.datn.doffice.dto.MeetingDTO;
import com.datn.doffice.dto.StaffDTO;
import com.datn.doffice.entity.MeetingAttendEntity;
import com.datn.doffice.entity.MeetingEntity;

public interface MeetingService {
	/**
	 * Tạo cuộc họp mới
	 */
	void createMeeting(MeetingDTO meetingDTO, UserLoginDetailDTO userLoginDetailDTO);

	/**
	 * Lấy tất cả danh sách công việc
	 */
	List<MeetingEntity> getAllMeeting();

	/**
	 * Lấy thông tin cuộc họp theo Id
	 */
	MeetingEntity getMeetingById(String meetingId);

	/**
	 * Xoá cuộc họp
	 */
	MeetingEntity deleteMeeting(String meetingId);

	/**
	 * Cập nhật thông tin cuộc họp
	 */
	void updateMeetingByForm(String meetingId, MeetingDTO meetingDTO, UserLoginDetailDTO userLoginDetailDTO);
	
	

	void updateMeetingDetail(String meetingId, MeetingEntity meeting,UserLoginDetailDTO userLoginDetailDTO);

	/**
	 * Huỷ cuộc họp
	 */

	void cancelMeeting(String meetingId, UserLoginDetailDTO userLoginDetailDTO);

	/**
	 * Lấy danh sách nhân viên của cuộc họp
	 * 
	 */
	List<AttendDTO> getAttendByMeetingId(String meetingId);
	
	/**
	 * Danh sach cuoc hop cua nhan vien
	 */
	List<AttendDTO> getAttendByUserId(String userId);

}
