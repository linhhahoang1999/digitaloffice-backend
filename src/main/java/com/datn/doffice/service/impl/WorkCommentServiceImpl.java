package com.datn.doffice.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datn.doffice.dao.TaskCollection;
import com.datn.doffice.dao.UserCollection;
import com.datn.doffice.dao.WorkCollection;
import com.datn.doffice.dao.WorkCommentCollection;
import com.datn.doffice.dto.CommentDTO;
import com.datn.doffice.dto.UserLoginDetailDTO;
import com.datn.doffice.entity.TaskEntity;
import com.datn.doffice.entity.UserEntity;
import com.datn.doffice.entity.WorkCommentEntity;
import com.datn.doffice.entity.WorkEntity;
import com.datn.doffice.service.WorkCommentService;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WorkCommentServiceImpl implements WorkCommentService {
	@Autowired
	WorkCommentCollection commentCollection;

	@Autowired
	WorkCollection workCollection;

	@Autowired
	TaskCollection taskCollection;
	@Autowired
	UserCollection userCollection;

	@Override
	public CommentDTO addComment(CommentDTO createComment, String userId) {
		WorkCommentEntity comment = 
				WorkCommentEntity.builder().body(createComment.getBody())
				.type(createComment.getType())
				.entityId(createComment.getEntityId())
				.userId(userId).parentId(createComment.getParentId())
				.createdAt(new Date()).isDeleted(false).build();
		UserEntity user = userCollection.getById(userId);
		commentCollection.insertObject(comment);
		return converToDTO(comment, user.getUserName());
	}

	@Override
	public List<CommentDTO> getCommentsByWork(String workId) {
		List<CommentDTO> rs = new ArrayList<CommentDTO>();
		WorkEntity work = workCollection.findById(workId);

		if (work != null) {
			List<WorkCommentEntity> comments = commentCollection.getListWorkComment(workId);
			if (comments != null) {
				for (WorkCommentEntity comment : comments) {
					UserEntity user = userCollection.getById(comment.getUserId());
					CommentDTO cmt = converToDTO(comment, user.getUserName());
					rs.add(cmt);

				}

			}

		}

		return rs;
	}

	@Override
	public List<CommentDTO> getCommentsByTask(String taskId) {
		List<CommentDTO> rs = new ArrayList<CommentDTO>();
		TaskEntity task = taskCollection.findById(taskId);
		if (task != null) {
			List<WorkCommentEntity> comments = commentCollection.getListTaskComment(taskId);
			if (comments != null) {
				for (WorkCommentEntity comment : comments) {
					UserEntity user = userCollection.getById(comment.getUserId());
					CommentDTO cmt = converToDTO(comment, user.getUserName());
					rs.add(cmt);

				}

			}
		}

		return rs;
	}

	@Override
	public CommentDTO updateComment(String text, String commentId) {
		WorkCommentEntity comment = commentCollection.findById(commentId);
		if (text != null)
			comment.setBody(text);
		else
			System.err.println("Khong co noi dung");
		commentCollection.updateOject(comment);
		UserEntity user = userCollection.getById(comment.getUserId());
		CommentDTO cmt = converToDTO(comment, user.getUserName());
		return cmt;
	}

	@Override
	public void deleteComment(String commentId) {
		commentCollection.deleteComment(commentId);

	}

	@Override
	public CommentDTO findById(String commentId) {
		WorkCommentEntity comment = commentCollection.findById(commentId);
		UserEntity user = userCollection.getById(comment.getUserId());
		CommentDTO cmt = converToDTO(comment, user.getUserName());
		return cmt;
	}

	public CommentDTO converToDTO(WorkCommentEntity comment, String userName) {
		CommentDTO cmt = CommentDTO.builder().id(comment.getId()).body(comment.getBody()).type(comment.getType())
				.entityId(comment.getEntityId()).userId(comment.getUserId()).userName(userName)
				.createdAt(comment.getCreatedAt()).parentId(comment.getParentId()).build();

		return cmt;
	}

}
