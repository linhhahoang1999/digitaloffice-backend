package com.datn.doffice.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datn.doffice.dao.MeetingAttendCollection;
import com.datn.doffice.dao.MeetingCollection;
import com.datn.doffice.dao.UserCollection;
import com.datn.doffice.dto.AttendDTO;
import com.datn.doffice.dto.MeetingDTO;
import com.datn.doffice.dto.StaffDTO;
import com.datn.doffice.dto.UserLoginDetailDTO;
import com.datn.doffice.entity.MeetingAttendEntity;
import com.datn.doffice.entity.MeetingEntity;
import com.datn.doffice.entity.TaskAssignEntity;
import com.datn.doffice.entity.TaskEntity;
import com.datn.doffice.entity.UserEntity;
import com.datn.doffice.exceptions.IdInvalidException;
import com.datn.doffice.exceptions.MeetingNotFoundException;
import com.datn.doffice.service.MeetingService;
import com.datn.doffice.utils.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MeetingServiceImpl implements MeetingService {

	@Autowired
	private MeetingCollection meetingCollection;

	@Autowired
	private MeetingAttendCollection meetingAttendCollection;

	@Autowired
	private UserCollection userCollection;

	@Override
	public void createMeeting(MeetingDTO meetingDTO, UserLoginDetailDTO userLoginDetailDTO) {

		MeetingEntity meetingEntity = MeetingEntity.builder().title(meetingDTO.getTitle()).venue(meetingDTO.getVenue())
				.start(meetingDTO.getStart()).end(meetingDTO.getEnd()).isCanceled(false).isDeleted(false).build();

		// Lưu cuộc họp
		meetingCollection.insertObject(meetingEntity);
		String meetingId = meetingEntity.getId();
		// Lưu danh sách nhân viên sẽ tham gia cuộc họp

		List<String> userAttends = meetingDTO.getUserAttend();
		for (String userid : userAttends) {
			MeetingAttendEntity meetingAttendEntity = MeetingAttendEntity.builder().meetingId(meetingId).userId(userid)
					.isConfirm(false).isAttend(true).build();

			meetingAttendCollection.insertObject(meetingAttendEntity);

		}

	}

	@Override
	public List<MeetingEntity> getAllMeeting() {

		return meetingCollection.findAll();
	}

	@Override
	public MeetingEntity getMeetingById(String meetingId) {
		return meetingCollection.findById(meetingId);
	}

	@Override
	public MeetingEntity deleteMeeting(String meetingId) {
		return meetingCollection.deletedMeeting(meetingId);
	}

	@Override
	public void updateMeetingByForm(String meetingId, MeetingDTO meetingDTO, UserLoginDetailDTO userLoginDetailDTO) {

		if (CommonUtils.isNullOrEmpty(meetingId))
			throw new IdInvalidException();

		MeetingEntity meetingEntity = meetingCollection.findById(meetingId);
		if (meetingEntity == null)
			throw new MeetingNotFoundException();

		if (meetingDTO.getTitle() != null)
			meetingEntity.setTitle(meetingDTO.getTitle());
		if (meetingDTO.getVenue() != null)
			meetingEntity.setVenue(meetingDTO.getVenue());
		if (meetingDTO.getStart() != null)
			meetingEntity.setStart(meetingDTO.getStart());
		if (meetingDTO.getEnd() != null)
			meetingEntity.setEnd(meetingDTO.getEnd());
		else
			System.err.println("Lỗi sửa cuộc họp ");

		meetingCollection.updateOject(meetingEntity);

	}

	@Override
	public void updateMeetingDetail(String meetingId, MeetingEntity meeting, UserLoginDetailDTO userLoginDetailDTO) {

		if (CommonUtils.isNullOrEmpty(meetingId))
			throw new IdInvalidException();

		MeetingEntity meetingEntity = meetingCollection.findById(meetingId);
		if (meetingEntity == null)
			throw new MeetingNotFoundException();

		if (meeting.getTitle() != null)
			meetingEntity.setTitle(meeting.getTitle());
		if (meeting.getVenue() != null)
			meetingEntity.setVenue(meeting.getVenue());
		if (meeting.getStart() != null)
			meetingEntity.setStart(meeting.getStart());
		if (meeting.getEnd() != null)
			meetingEntity.setEnd(meeting.getEnd());
		if (meeting.getIsCanceled() != null)
			meetingEntity.setIsCanceled(meeting.getIsCanceled());
		else
			System.err.println(meeting.toString());

		meetingCollection.updateOject(meetingEntity);

	}

	@Override
	public List<AttendDTO> getAttendByMeetingId(String meetingId) {

		List<MeetingAttendEntity> tmp = meetingAttendCollection.getMeetingAttendByMeetingId(meetingId);
		List<AttendDTO> rs = new ArrayList<AttendDTO>();
		for (MeetingAttendEntity a : tmp) {
			UserEntity user = userCollection.findById(a.getUserId().replace("\"", ""));
			AttendDTO staff = AttendDTO.builder().id(a.getId()).userId(user.getId().replace("\"", ""))
					.fullName(user.getFullName()).email(user.getFullName()).isAttend(a.getIsAttend())
					.isConfirm(a.getIsConfirm()).userName(user.getUserName()).build();
			rs.add(staff);
		}
		return rs;
	}

	@Override
	public void cancelMeeting(String meetingId, UserLoginDetailDTO userLoginDetailDTO) {
		if (CommonUtils.isNullOrEmpty(meetingId))
			throw new IdInvalidException();

		MeetingEntity meetingEntity = meetingCollection.findById(meetingId);
		if (meetingEntity == null)
			throw new MeetingNotFoundException();

		meetingEntity.setIsCanceled(true);
		meetingCollection.updateOject(meetingEntity);

	}

	@Override
	public List<AttendDTO> getAttendByUserId(String userId) {
		// TODO Auto-generated method stub
		List<MeetingAttendEntity> tmp = meetingAttendCollection.getMeetingAttendByUserId(userId);
		List<AttendDTO> rs = new ArrayList<AttendDTO>();
		for (MeetingAttendEntity a : tmp) {
		    MeetingEntity m = 	meetingCollection.findById(a.getMeetingId().replace("\"", ""));
			AttendDTO staff = AttendDTO.builder().id(a.getId()).userId(userId)
					.isAttend(a.getIsAttend()).start(m.getStart()).meetingId(m.getId())
					.isConfirm(a.getIsConfirm()).build();
			rs.add(staff);
		}
		
		return rs;
	}

}
