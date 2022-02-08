package com.datn.doffice.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.datn.doffice.annotation.Api;
import com.datn.doffice.dto.StaffDTO;
import com.datn.doffice.dto.TaskDTO;
import com.datn.doffice.dto.UserLoginDetailDTO;
import com.datn.doffice.dto.WorkDTO;
import com.datn.doffice.entity.HistoryEntity;
import com.datn.doffice.entity.TaskAssignEntity;
import com.datn.doffice.entity.TaskEntity;
import com.datn.doffice.entity.WorkEntity;
import com.datn.doffice.enums.ApiError;
import com.datn.doffice.enums.ApiStatus;
import com.datn.doffice.service.TaskService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api(path = "/api/task")
public class TaskController extends ApiController {

	@Autowired
	private TaskService taskService;

	@GetMapping()
	public ResponseEntity<?> getAllTasks() {
		List<TaskEntity> rs = taskService.getAllTask();
		return ok(rs);
	}

	@GetMapping("/search/{searchPhrase}")
	public ResponseEntity<?> searchTask(@PathVariable String searchPhrase) {
		List<TaskEntity> rs = taskService.searchTask(searchPhrase);
		return ok(rs);
	}

	@PostMapping()
	public ResponseEntity<?> createTaskByForm(@ModelAttribute TaskDTO taskDTO, HttpServletRequest request) {
		UserLoginDetailDTO curUser = getCurrentUser(request);
		try {
			taskService.createTask(taskDTO, curUser);

		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			return error(ApiError.INTERNAL_SERVER_ERROR, request);
		}
		return ok(ApiStatus.OK);

	}

	@GetMapping("/{taskId}")
	public ResponseEntity<?> getTaskById(@PathVariable String taskId) {
		TaskEntity rs = taskService.getTaskById(taskId);
		return ok(rs);
	}
	
	@GetMapping("/history/{taskId}")
	public ResponseEntity<?> getTaskHistory(@PathVariable String taskId) {
		List<HistoryEntity> rs = taskService.getTaskHistory(taskId);
		return ok(rs);
	}


	@GetMapping("/assign/{taskId}")
	public ResponseEntity<?> getAssignTaskById(@PathVariable String taskId) {
//		System.out.println("gọi api");
		List<StaffDTO> rs = taskService.getTaskAssignByTaskId(taskId);
		return ok(rs);
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<?> getTaskByUser(@PathVariable String userId) {
//		System.out.println("gọi api");
		List<TaskEntity> rs = taskService.getTaskByUserId(userId);
		return ok(rs);
	}

	@GetMapping("/work/{workId}")
	public ResponseEntity<?> getTaskByWorkId(@PathVariable String workId) {
		List<TaskEntity> rs = taskService.getTaskByWorkId(workId);
		return ok(rs);
	}

	@DeleteMapping("/{taskId}")
	public ResponseEntity<?> deleteTask(@PathVariable String taskId,HttpServletRequest request) {
		UserLoginDetailDTO curUser = getCurrentUser(request);
		TaskEntity rs = taskService.deleteTask(taskId,curUser);
		return ok(rs);

	}

	@PutMapping("/{taskId}")
	public ResponseEntity<?> updateTask(@PathVariable String taskId, @ModelAttribute TaskDTO taskDTO,
			HttpServletRequest request) {
//		System.out.println(taskDTO.getTitle());
		UserLoginDetailDTO curUser = getCurrentUser(request);
		taskService.updateTask(taskId, taskDTO, curUser);
		return ok(ApiStatus.OK);
	}

	@PostMapping("/assign/taskId={taskId}/userId={userId}")
	public ResponseEntity<?> insertTaskkAssign(@PathVariable String taskId, @PathVariable String userId) {

		try {
			TaskAssignEntity rs = taskService.insertTaskAssign(taskId, userId);
			return ok(rs);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return ok(ApiStatus.OK);
	}

	// xoá nhân viên tham gia
	@DeleteMapping("/assign/taskId={taskId}/userId={userId}")
	public ResponseEntity<?> deleteTaskAssign(@PathVariable String taskId, @PathVariable String userId) {
		try {
			taskService.removeTaskAssign(taskId, userId);
			return ok(ApiStatus.OK);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return ok(ApiStatus.OK);
	}

}
