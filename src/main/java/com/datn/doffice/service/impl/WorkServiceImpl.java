package com.datn.doffice.service.impl;

import java.io.Console;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datn.doffice.dao.HistoryCollection;
import com.datn.doffice.dao.UserCollection;
import com.datn.doffice.dao.WorkAssignCollection;
import com.datn.doffice.dao.WorkCollection;
import com.datn.doffice.dto.StaffDTO;
import com.datn.doffice.dto.UserLoginDetailDTO;
import com.datn.doffice.dto.WorkDTO;
import com.datn.doffice.entity.HistoryEntity;
import com.datn.doffice.entity.UserEntity;
import com.datn.doffice.entity.WorkAssignEntity;
import com.datn.doffice.entity.WorkEntity;
import com.datn.doffice.exceptions.IdInvalidException;
import com.datn.doffice.exceptions.WorkNotFoundException;

import com.datn.doffice.service.WorkService;
import com.datn.doffice.utils.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WorkServiceImpl implements WorkService {

	@Autowired
	private WorkCollection workCollection;

	@Autowired
	private UserCollection userCollection;

	@Autowired
	private WorkAssignCollection workAssignCollection;
	@Autowired
	private HistoryCollection historyCollection;

	@Override
	public void createWork(WorkDTO workDTO, UserLoginDetailDTO userLoginDetailDTO) {

		WorkEntity workEntity = WorkEntity.builder().title(workDTO.getTitle()).description(workDTO.getDescription())
				.beginDate(workDTO.getBeginDate()).endDate(workDTO.getEndDate()).createdAt(new Date())
				.createdBy(userLoginDetailDTO.getUserId()).lastEdited(new Date())
				.lastEditedBy(userLoginDetailDTO.getUserId()).isStored(false).isDeleted(false).isCompleted(false)
				.build();

		// Lưu công việc
		workCollection.insertObject(workEntity);
		String workId = workEntity.getId();
		// Lưu danh sách nhân viên
		if (workDTO.getUserAssign().size() > 0) {
			List<String> userAssigns = workDTO.getUserAssign();

			for (String userid : userAssigns) {
				WorkAssignEntity workAssignEntity = WorkAssignEntity.builder().workId(workId).userId(userid).build();
				workAssignCollection.insertObject(workAssignEntity);

			}

		}
		// Lưu lịch sử
		UserEntity user = userCollection.findById(userLoginDetailDTO.getUserId().replace("\"", ""));
		HistoryEntity history = HistoryEntity.builder().entityId(workEntity.getId()).userId(user.getId())
				.userName(user.getUserName()).type("work").action("Tạo")
				.description("Tạo Công việc: " + workEntity.getTitle()).createdAt(new Date()).build();
		historyCollection.insertObject(history);

	}

	@Override
	public List<WorkEntity> getAllWork() {
		return workCollection.findAll();
	}

	@Override
	public WorkEntity getWorkById(String workId) {
		return workCollection.findById(workId);
	}

	@Override
	public WorkEntity deleteWork(String workId) {
		return workCollection.deleteWork(workId);
	}

	@Override
	public void updateWork(String workId, WorkDTO workDTO, UserLoginDetailDTO userLoginDetailDTO) {

		if (CommonUtils.isNullOrEmpty(workId))
			throw new IdInvalidException();

		WorkEntity workEntity = workCollection.findById(workId);
		WorkEntity previous = workCollection.findById(workId);

		if (workEntity == null)
			throw new WorkNotFoundException();

		workEntity.setTitle(workDTO.getTitle());
		workEntity.setDescription(workDTO.getDescription());
		workEntity.setBeginDate(workDTO.getBeginDate());
		workEntity.setEndDate(workDTO.getEndDate());
		workEntity.setLastEdited(new Date());
		workEntity.setLastEditedBy(userLoginDetailDTO.getUserId());

		workCollection.updateObject(workEntity);
		// Lưu lịch sử
		UserEntity user = userCollection.findById(userLoginDetailDTO.getUserId().replace("\"", ""));
		insertHistory(user, previous, workEntity);

	}

	@Override
	public List<String> getWorkAssignByWorkId(String workId) {

		return workAssignCollection.getWorkAssignByWorkId(workId);
	}

	@Override
	public List<StaffDTO> getAssignByWorkId(String workId) {
		List<String> ListUserId = workAssignCollection.getWorkAssignByWorkId(workId);
		List<StaffDTO> rs = new ArrayList<StaffDTO>();

		for (String id : ListUserId) {
			UserEntity user = userCollection.findById(id);
			StaffDTO staff = StaffDTO.builder().id(user.getId()).fullName(user.getFullName()).email(user.getFullName())
					.userName(user.getUserName()).build();
			rs.add(staff);
		}

		return rs;
	}

	@Override
	public WorkAssignEntity insertNewWorkAssign(String workId, String userId) {
		WorkAssignEntity workAssignEntity = WorkAssignEntity.builder().workId(workId).userId(userId).build();

		System.out.println("thêm nhân viên vào công việc");
		workAssignCollection.insertObject(workAssignEntity);
		return workAssignEntity;
	}

	@Override
	public void removeWorkAssign(String workId, String userId) {
		workAssignCollection.deleteWorkAssign(workId, userId);
	}

	@Override
	public List<WorkEntity> getWorkByUserId(String userId) {

		return workCollection.findWorkByUser(userId);
	}

	@Override
	public void updateWorkDetail(String workId, WorkEntity work, UserLoginDetailDTO userLoginDetailDTO) {

		if (CommonUtils.isNullOrEmpty(workId))
			throw new IdInvalidException();

		WorkEntity workEntity = workCollection.findById(workId);

		WorkEntity previous = workCollection.findById(workId);
		if (workEntity == null)
			throw new WorkNotFoundException();

		if (work.getTitle() != null)
			workEntity.setTitle(work.getTitle());
		if (work.getDescription() != null)
			workEntity.setDescription(work.getDescription());
		if (work.getBeginDate() != null)
			workEntity.setBeginDate(work.getBeginDate());
		if (work.getEndDate() != null)
			workEntity.setEndDate(work.getEndDate());
		if (work.getIsCompleted() != null)
			workEntity.setIsCompleted(work.getIsCompleted());
		if (work.getIsDeleted() != null)
			workEntity.setIsDeleted(work.getIsDeleted());
		if (work.getIsStored() != null)
			workEntity.setIsStored(work.getIsStored());
		else
			System.err.println("Lỗi update công việc");

		workEntity.setLastEdited(new Date());
		workEntity.setLastEditedBy(userLoginDetailDTO.getUserId());

		workCollection.updateObject(workEntity);

		UserEntity user = userCollection.findById(userLoginDetailDTO.getUserId().replace("\"", ""));
		insertHistory(user, previous, workEntity);

	}

	@Override
	public List<WorkEntity> getAllStoredWorks() {
		return workCollection.findAllStoredWorks();
	}

	@Override
	public List<WorkEntity> searchWork(String searchPhrase) {

		return workCollection.fullTextSearch(searchPhrase);
	}

	@Override
	public List<HistoryEntity> getWorkHistory(String workId) {
		return historyCollection.getHistoryOfWork(workId);
	}

	public void insertHistory(UserEntity user, WorkEntity previous, WorkEntity workEntity) {

//		System.err.println(previous + "\n " + workEntity);
		HistoryEntity history = HistoryEntity.builder().entityId(workEntity.getId()).userId(user.getId())
				.userName(user.getUserName()).type("work").action("Cập nhật").description("Cập nhật thông tin công việc:\n")
				.createdAt(new Date()).build();

		if (!previous.getTitle().equals(workEntity.getTitle())) {
			String tmp = history.getDescription();
			tmp += "Thay đổi tiêu đề công việc: " + previous.getTitle() + " thành: " + workEntity.getTitle() + ".\n";
			history.setDescription(tmp);
		}
		if (!previous.getDescription().equals(workEntity.getDescription())) {
			String tmp = history.getDescription();
			tmp += "Thay đổi mô tả công việc.\n ";
			history.setDescription(tmp);
		}
		if (previous.getBeginDate().compareTo(workEntity.getBeginDate()) != 0
				|| previous.getEndDate().compareTo(workEntity.getEndDate()) != 0) {
			String tmp = history.getDescription();
			tmp += "Thay đổi thời gian công việc.\n";
			history.setDescription(tmp);
		}

		if (workEntity.getIsCompleted() != previous.getIsCompleted())
			history.setDescription("Thay đổi trạng thái công việc");

		if (previous.getIsStored() == true && (workEntity.getIsStored() != previous.getIsStored()))
			history.setDescription("Xoá công việc khỏi danh sách lưu trữ");

		if (previous.getIsStored() == false && (workEntity.getIsStored() != previous.getIsStored()))
			history.setDescription("Thêm công việc vào danh sách lưu trữ");

		historyCollection.insertObject(history);
	}

}
