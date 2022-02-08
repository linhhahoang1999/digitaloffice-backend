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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.datn.doffice.annotation.Api;
import com.datn.doffice.dto.StaffDTO;
import com.datn.doffice.dto.UserLoginDetailDTO;
import com.datn.doffice.dto.WorkDTO;
import com.datn.doffice.entity.HistoryEntity;
import com.datn.doffice.entity.WorkAssignEntity;
import com.datn.doffice.entity.WorkEntity;
import com.datn.doffice.enums.ApiError;
import com.datn.doffice.enums.ApiStatus;
import com.datn.doffice.service.WorkService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Api(path = "/api/work")
public class WorkController extends ApiController {

	@Autowired
	private WorkService workService;

	@GetMapping()
	public ResponseEntity<?> getAllWorks() {
		List<WorkEntity> rs = workService.getAllWork();
		return ok(rs);
	}

	@GetMapping("/search/{searchPhrase}")
	public ResponseEntity<?> searchWork(@PathVariable String searchPhrase) {
		System.out.println("search Work");
		List<WorkEntity> rs = workService.searchWork(searchPhrase);
		return ok(rs);
	}

	@GetMapping("/stored")
	public ResponseEntity<?> getAllStoredWorks() {
		List<WorkEntity> rs = workService.getAllStoredWorks();
		return ok(rs);
	}

	@PostMapping()
	public ResponseEntity<?> createWorkByForm(@ModelAttribute WorkDTO workDTO, HttpServletRequest request) {
		UserLoginDetailDTO curUser = getCurrentUser(request);
		try {
			workService.createWork(workDTO, curUser);

		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			return error(ApiError.INTERNAL_SERVER_ERROR, request);
		}
		return ok(ApiStatus.OK);

	}

	@PostMapping("/assign/workId={workId}/userId={userId}")
	public ResponseEntity<?> createWorkAssign(@PathVariable String workId, @PathVariable String userId) {

		WorkAssignEntity rs = workService.insertNewWorkAssign(workId, userId);
		return ok(rs);

	}

	@GetMapping("/{workId}")
	public ResponseEntity<?> getWorkById(@PathVariable String workId) {
		WorkEntity rs = workService.getWorkById(workId);
		return ok(rs);
	}
	
	@GetMapping("/history/{workId}")
	public ResponseEntity<?> getWorkHistory(@PathVariable String workId) {
		List<HistoryEntity> rs = workService.getWorkHistory(workId);
		return ok(rs);
	}

	@GetMapping("/assign/{workId}")
	public ResponseEntity<?> getAssignkByWorkId(@PathVariable String workId) {
		List<StaffDTO> rs = workService.getAssignByWorkId(workId);

		return ok(rs);
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<?> getWorkByUser(@PathVariable String userId) {

		List<WorkEntity> rs = workService.getWorkByUserId(userId);
		return ok(rs);
	}

	@DeleteMapping("/{workId}")
	public ResponseEntity<?> deleteWork(@PathVariable String workId) {
		WorkEntity rs = workService.deleteWork(workId);
		return ok(rs);
	}

	@PutMapping("/{workId}")
	public ResponseEntity<?> updateWork(@PathVariable String workId, @ModelAttribute WorkDTO workDTO,
			HttpServletRequest request) {
//		System.out.println(workDTO.getDescription());
		UserLoginDetailDTO curUser = getCurrentUser(request);
		workService.updateWork(workId, workDTO, curUser);
		return ok(ApiStatus.OK);
	}

	@PutMapping("/update/{workId}")
	public ResponseEntity<?> updateWorkDetail(@PathVariable String workId, @RequestBody WorkEntity work,
			HttpServletRequest request) {

		UserLoginDetailDTO curUser = getCurrentUser(request);
		workService.updateWorkDetail(workId, work, curUser);
		return ok(ApiStatus.OK);
	}

	@DeleteMapping("/assign/workId={workId}/userId={userId}")
	public ResponseEntity<?> deleteWorkAssign(@PathVariable String workId, @PathVariable String userId) {

		try {
			workService.removeWorkAssign(workId, userId);
			return ok(ApiStatus.OK);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return ok(ApiStatus.OK);
	}

}
