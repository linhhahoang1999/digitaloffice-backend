package com.datn.doffice.service;

import java.util.List;

import com.datn.doffice.dto.StaffDTO;
import com.datn.doffice.dto.UserLoginDetailDTO;
import com.datn.doffice.dto.WorkDTO;
import com.datn.doffice.entity.HistoryEntity;
import com.datn.doffice.entity.WorkAssignEntity;
import com.datn.doffice.entity.WorkEntity;

public interface WorkService {

	/**
	 * Tạo công việc mới
	 */
	void createWork(WorkDTO workDTO, UserLoginDetailDTO userLoginDetailDTO);

	/**
	 * Lấy tất cả danh sách công việc
	 */
	List<WorkEntity> getAllWork();
	
	List<WorkEntity> searchWork(String searchPhrase);
	
	/**
	 * Lấy tất cả danh sách công việc được lưu trữ
	 */
	List<WorkEntity> getAllStoredWorks();

	/**
	 * Lấy công việc theo Id
	 */
	WorkEntity getWorkById(String workId);

	/**
	 * Xoá công việc
	 */
	WorkEntity deleteWork(String workId);

	/**
	 * Cập nhật thông tin công việc
	 */
	void updateWork(String workId, WorkDTO workDTO, UserLoginDetailDTO userLoginDetailDTO);
	
	void updateWorkDetail(String workId, WorkEntity work, UserLoginDetailDTO userLoginDetailDTO);
	

	/**
	 * Lấy danh sách nhân viên của công việc
	 * 
	 */
	List<String> getWorkAssignByWorkId(String workId);

	List<StaffDTO> getAssignByWorkId(String workId);

	/**
	 * Lấy danh sách công việc của user
	 * 
	 */
	List<WorkEntity> getWorkByUserId(String userId);
	
	/**
	 * quản lý danh sách nhân viên tham gia công việc
	 * 
	 */
	WorkAssignEntity insertNewWorkAssign(String workId, String userId);
		

	void removeWorkAssign(String workId, String userId);
	
	/**
	 * Lịch sử chỉnh sửa công việc
	 * */
	
	List<HistoryEntity> getWorkHistory(String workId);
	



}
