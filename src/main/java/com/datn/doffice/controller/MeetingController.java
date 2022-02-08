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

import com.datn.doffice.annotation.Api;
import com.datn.doffice.dao.MeetingCollection;
import com.datn.doffice.dto.AttendDTO;
import com.datn.doffice.dto.MeetingDTO;
import com.datn.doffice.dto.StaffDTO;
import com.datn.doffice.dto.TaskDTO;
import com.datn.doffice.dto.UserLoginDetailDTO;
import com.datn.doffice.entity.MeetingAttendEntity;
import com.datn.doffice.entity.MeetingEntity;
import com.datn.doffice.entity.TaskEntity;
import com.datn.doffice.enums.ApiError;
import com.datn.doffice.enums.ApiStatus;
import com.datn.doffice.service.MeetingService;
import com.datn.doffice.service.TaskService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api(path = "/api/meeting")
public class MeetingController extends ApiController {

	@Autowired
	private MeetingService meetingService;

	@GetMapping()
	public ResponseEntity<?> getAllMeetings() {
		List<MeetingEntity> rs = meetingService.getAllMeeting();
		return ok(rs);
	}

	@PostMapping()
	public ResponseEntity<?> createTaskByForm(@ModelAttribute MeetingDTO meetingDTO, HttpServletRequest request) {
		UserLoginDetailDTO curUser = getCurrentUser(request);
		try {
			meetingService.createMeeting(meetingDTO, curUser);

		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			return error(ApiError.INTERNAL_SERVER_ERROR, request);
		}
		return ok(ApiStatus.OK);

	}

	

	@GetMapping("/{meetingId}")
	public ResponseEntity<?> getMeetingById(@PathVariable String meetingId) {
		MeetingEntity rs = meetingService.getMeetingById(meetingId);
		return ok(rs);
	}

	@GetMapping("/attend/{meetingId}")
	public ResponseEntity<?> getAttendByMeetingId(@PathVariable String meetingId) {
		List<AttendDTO> rs = meetingService.getAttendByMeetingId(meetingId);
		return ok(rs);
	}
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<?> getAttendByUserId(@PathVariable String userId) {
		List<AttendDTO> rs = meetingService.getAttendByUserId(userId);
		return ok(rs);
	}


	@DeleteMapping("/{meetingId}")
	public ResponseEntity<?> deleteMeeting(@PathVariable String meetingId) {
		System.out.println("xoá cuộc họp : " + meetingId);
		MeetingEntity rs = meetingService.deleteMeeting(meetingId);
		return ok(rs);

	}

	@PutMapping("/{meetingId}")
	public ResponseEntity<?> updateMeeting(@PathVariable String meetingId, @ModelAttribute MeetingDTO meetingDTO,
			HttpServletRequest request) {
//		System.out.println(taskDTO.getTitle());
		UserLoginDetailDTO curUser = getCurrentUser(request);
		meetingService.updateMeetingByForm(meetingId, meetingDTO, curUser);
		return ok(ApiStatus.OK);
	}

	@PutMapping("/update/{meetingId}")
	public ResponseEntity<?> updateMeetingDetail(@PathVariable String meetingId, @RequestBody MeetingEntity meeting,
			HttpServletRequest request) {
//		System.out.println(taskDTO.getTitle());
		UserLoginDetailDTO curUser = getCurrentUser(request);
		meetingService.updateMeetingDetail(meetingId, meeting, curUser);
		return ok(ApiStatus.OK);
	}

}
