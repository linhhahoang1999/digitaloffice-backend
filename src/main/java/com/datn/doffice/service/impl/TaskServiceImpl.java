package com.datn.doffice.service.impl;

import java.io.Console;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datn.doffice.dao.UserCollection;
import com.datn.doffice.dao.WorkCollection;
import com.datn.doffice.dao.HistoryCollection;
import com.datn.doffice.dao.TaskAssignCollection;
import com.datn.doffice.dao.TaskCollection;
import com.datn.doffice.dto.UserLoginDetailDTO;
import com.datn.doffice.dto.StaffDTO;
import com.datn.doffice.dto.TaskDTO;
import com.datn.doffice.entity.HistoryEntity;
import com.datn.doffice.entity.TaskAssignEntity;
import com.datn.doffice.entity.TaskEntity;
import com.datn.doffice.entity.UserEntity;
import com.datn.doffice.entity.WorkEntity;
import com.datn.doffice.exceptions.IdInvalidException;
import com.datn.doffice.exceptions.TaskNotFoundException;
import com.datn.doffice.service.MailService;
import com.datn.doffice.service.TaskService;
import com.datn.doffice.utils.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {
	@Autowired
	private TaskCollection taskCollection;

	@Autowired
	private TaskAssignCollection taskAssignCollection;
	@Autowired
	private UserCollection userCollection;

	@Autowired
	private HistoryCollection historyCollection;

	@Override
	public void createTask(TaskDTO taskDTO, UserLoginDetailDTO userLoginDetailDTO) {

		TaskEntity taskEntity = TaskEntity.builder().workId(taskDTO.getWorkId()).title(taskDTO.getTitle())
				.description(taskDTO.getDescription()).priority(taskDTO.getPriority()).status(taskDTO.getStatus())
				.beginDate(taskDTO.getBeginDate()).endDate(taskDTO.getEndDate()).createdAt(new Date())
				.createdBy(userLoginDetailDTO.getUserId()).lastEdited(new Date())
				.lastEditedBy(userLoginDetailDTO.getUserId()).isDeleted(false).build();

		// Lưu Tác vụ
		taskCollection.insertObject(taskEntity);
		String taskId = taskEntity.getId();
		// Lưu danh sách nhân viên

		List<String> userAssigns = taskDTO.getUserAssign();

		for (String userid : userAssigns) {
			TaskAssignEntity taskAssignEntity = TaskAssignEntity.builder().taskId(taskId).userId(userid).build();

			taskAssignCollection.insertObject(taskAssignEntity);

		}
		// Lưu lịch sử
		UserEntity user = userCollection.findById(userLoginDetailDTO.getUserId().replace("\"", ""));
		HistoryEntity history = HistoryEntity.builder().entityId(taskEntity.getId()).userId(user.getId())
				.userName(user.getUserName()).type("task").action("Tạo")
				.description("Tạo tác vụ: " + taskEntity.getTitle()).createdAt(new Date()).build();
		historyCollection.insertObject(history);
		// Lưu lịch sử công việc
		HistoryEntity workHistory = HistoryEntity.builder().entityId(taskEntity.getWorkId()).userId(user.getId())
				.userName(user.getUserName()).type("work").action("Tạo")
				.description("Tạo tác vụ: " + taskEntity.getTitle()).createdAt(new Date()).build();
		historyCollection.insertObject(workHistory);

	}

	@Override
	public List<TaskEntity> getAllTask() {
		return taskCollection.findAll();
	}

	@Override
	public TaskEntity getTaskById(String taskId) {
		return taskCollection.findById(taskId);
	}

	@Override
	public TaskEntity deleteTask(String taskId, UserLoginDetailDTO userLoginDetailDTO) {

		TaskEntity taskEntity = taskCollection.findById(taskId);
		UserEntity user = userCollection.findById(userLoginDetailDTO.getUserId().replace("\"", ""));
		// Lưu lịch sử công việc
		HistoryEntity workHistory = HistoryEntity.builder().entityId(taskEntity.getWorkId()).userId(user.getId())
				.userName(user.getUserName()).type("work").action("cập nhật")
				.description("Xoá tác vụ: " + taskEntity.getTitle()).createdAt(new Date()).build();
		historyCollection.insertObject(workHistory);

		return taskCollection.deleteTask(taskId);
	}

	@Override
	public void updateTask(String taskId, TaskDTO taskDTO, UserLoginDetailDTO userLoginDetailDTO) {

		if (CommonUtils.isNullOrEmpty(taskId))
			throw new IdInvalidException();

		TaskEntity taskEntity = taskCollection.findById(taskId);
		TaskEntity previous = taskCollection.findById(taskId);
		if (taskEntity == null)
			throw new TaskNotFoundException();

		if (taskDTO.getTitle() != null) {
			taskEntity.setTitle(taskDTO.getTitle());
		}

		if (taskDTOValidate(taskDTO)) {
			taskEntity.setTitle(taskDTO.getTitle());
			taskEntity.setDescription(taskDTO.getDescription());
			taskEntity.setPriority(taskDTO.getPriority());
			taskEntity.setStatus(taskDTO.getStatus());
			taskEntity.setBeginDate(taskDTO.getBeginDate());
			taskEntity.setEndDate(taskDTO.getEndDate());
			taskEntity.setLastEdited(new Date());
			taskEntity.setLastEditedBy(userLoginDetailDTO.getUserId());
			taskCollection.updateOject(taskEntity);

		} else {
			System.err.println("Loi update Task");
		}

		// Lưu lịch sử
		UserEntity user = userCollection.findById(userLoginDetailDTO.getUserId().replace("\"", ""));
		insertHistory(user, previous, taskEntity);

	}

	private Boolean taskDTOValidate(TaskDTO taskDTO) {

		if (taskDTO == null)
			return false;

		if (taskDTO.getTitle() == null)
			taskDTO.setTitle("");

		if (taskDTO.getDescription() == null)
			taskDTO.setDescription(null);

		if (CommonUtils.isNullOrEmpty(taskDTO.getWorkId()))
			return false;

		if (taskDTO.getUserAssign() == null) {
			System.err.println("user null ");
			return false;
		}

		return true;

	}

	@Override
	public List<StaffDTO> getTaskAssignByTaskId(String taskId) {
//		System.out.println("lấy nhân viên tham gia task");
		List<TaskAssignEntity> listAssign = taskAssignCollection.getTaskAssignByTaskId(taskId);
		List<StaffDTO> rs = new ArrayList<StaffDTO>();
		for (TaskAssignEntity a : listAssign) {
			UserEntity user = userCollection.findById(a.getUserId().replace("\"", ""));
			StaffDTO staff = StaffDTO.builder().id(user.getId()).fullName(user.getFullName()).email(user.getFullName())
					.userName(user.getUserName()).build();
			rs.add(staff);
		}

		return rs;
	}

	@Override
	public List<TaskEntity> getTaskByWorkId(String WorkId) {
		// TODO Auto-generated method stub
		return taskCollection.getTaskByWorkId(WorkId);
	}

	@Override
	public TaskAssignEntity insertTaskAssign(String taskId, String userId) {

		TaskAssignEntity ta = TaskAssignEntity.builder().taskId(taskId).userId(userId).build();
		taskAssignCollection.insertObject(ta);

		return ta;
	}

	@Override
	public void removeTaskAssign(String taskId, String userId) {

		taskAssignCollection.removeTaskAssign(taskId, userId);

	}

	@Override
	public List<TaskEntity> getTaskByUserId(String userId) {

		return taskCollection.findByUser(userId);
	}

	@Override
	public List<TaskEntity> searchTask(String searchPhrase) {

		return taskCollection.searchTask(searchPhrase);
	}

	@Override
	public List<HistoryEntity> getTaskHistory(String taskId) {

		return historyCollection.getHistoryOfTask(taskId);
	}

	public void insertHistory(UserEntity user, TaskEntity previous, TaskEntity taskEntity) {

		System.err.println(previous + "\n " + taskEntity);
		HistoryEntity history = HistoryEntity.builder().entityId(taskEntity.getId()).userId(user.getId())
				.userName(user.getUserName()).type("task").action("Cập nhật")
				.description("Cập nhật thông tin tác vụ:\n").createdAt(new Date()).build();

		if (!previous.getTitle().equals(taskEntity.getTitle())) {
			String tmp = history.getDescription();
			tmp += "Thay đổi tiêu đề tác vụ: " + previous.getTitle() + " thành: " + taskEntity.getTitle() + ".\n";
			history.setDescription(tmp);
		}
		if (!previous.getDescription().equals(taskEntity.getDescription())) {
			String tmp = history.getDescription();
			tmp += "Thay đổi mô tả tác vụ.\n ";
			history.setDescription(tmp);
		}
		if (previous.getBeginDate().compareTo(taskEntity.getBeginDate()) != 0
				|| previous.getEndDate().compareTo(taskEntity.getEndDate()) != 0) {
			String tmp = history.getDescription();
			tmp += "Thay đổi thời gian tác vụ.\n";
			history.setDescription(tmp);
		}

		if (!taskEntity.getStatus().equals(previous.getStatus()) ) {
			String tmp = history.getDescription();
			tmp += "Thay đổi trạng thái tác vụ.\n";
			history.setDescription(tmp);
		}
			

		if (!taskEntity.getPriority().equals(previous.getPriority())) {
			String tmp = history.getDescription();
			tmp += "Thay đổi độ ưu tiên của tác vụ.\n";
			history.setDescription(tmp);
		}
			

		historyCollection.insertObject(history);
	}

}
