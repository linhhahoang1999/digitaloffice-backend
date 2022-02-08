package com.datn.doffice.service;

import java.util.List;

import com.datn.doffice.dto.UserLoginDetailDTO;
import com.datn.doffice.dto.StaffDTO;
import com.datn.doffice.dto.TaskDTO;
import com.datn.doffice.entity.HistoryEntity;
import com.datn.doffice.entity.TaskAssignEntity;
import com.datn.doffice.entity.TaskEntity;

public interface TaskService {

	/**
	 * Tạo tác vụ mới
	 */
	void createTask(TaskDTO taskDTO, UserLoginDetailDTO userLoginDetailDTO);

	/**
	 * Lấy tất cả danh sách tác vụ
	 */
	List<TaskEntity> getAllTask();

	/**
	 *Tìm kiếm tác vụ
	 */
	List<TaskEntity> searchTask(String searchPhrase);

	/**
	 * Lấy tác vụ theo Id
	 */
	TaskEntity getTaskById(String taskId);

	/**
	 * Xoá tác vụ
	 */
	TaskEntity deleteTask(String taskId, UserLoginDetailDTO userLoginDetailDTO);

	/**
	 * Cập nhật thông tin tác vụ
	 */
	void updateTask(String taskId, TaskDTO taskDTO, UserLoginDetailDTO userLoginDetailDTO);

	/**
	 * Lấy danh sách nhân viên của tác vụ
	 * 
	 */
	List<StaffDTO> getTaskAssignByTaskId(String taskId);

	/**
	 * Lấy danh sách tac vu cua nhan vien
	 * 
	 */
	List<TaskEntity> getTaskByUserId(String userId);

	/**
	 * Lấy danh sách các tác vụ của công việc
	 **/
	List<TaskEntity> getTaskByWorkId(String WorkId);

	TaskAssignEntity insertTaskAssign(String taskId, String userId);

	void removeTaskAssign(String taskId, String userId);
	
	/**
	 * Lịch sử tác vụ
	 **/
	List<HistoryEntity> getTaskHistory(String taskId);

}
