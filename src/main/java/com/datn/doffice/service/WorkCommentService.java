package com.datn.doffice.service;

import java.util.List;

import com.datn.doffice.dto.CommentDTO;
import com.datn.doffice.entity.WorkCommentEntity;

public interface WorkCommentService {

	/**
	 * Tạo công nhận xét mới
	 */
	CommentDTO addComment(CommentDTO createComment ,String userId );
	

	/**
	 * Tạo công nhận xét mới
	 */
	CommentDTO findById(String commentId);

	/**
	 * Lấy nhận xét của công việc
	 */
	List<CommentDTO> getCommentsByWork(String workId);

	/**
	 * Lấy nhận xét của tác vụ
	 */
	List<CommentDTO> getCommentsByTask(String taskId);

	/**
	 * cập nhật nhận xét
	 */
	CommentDTO updateComment(String text, String commentId);

	/**
	 * Xoá nhận xét
	 */
	void deleteComment(String commentId);

}
