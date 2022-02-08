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
import com.datn.doffice.dto.CommentDTO;
import com.datn.doffice.dto.UserLoginDetailDTO;
import com.datn.doffice.entity.WorkCommentEntity;
import com.datn.doffice.enums.ApiError;
import com.datn.doffice.enums.ApiStatus;
import com.datn.doffice.service.WorkCommentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api(path = "/api/comment")
public class WorkCommentController extends ApiController {
	@Autowired
	WorkCommentService commentService;

	@GetMapping("/work/{workId}")
	public ResponseEntity<?> getWorkComment(@PathVariable String workId) {
		List<CommentDTO> rs = commentService.getCommentsByWork(workId);
		return ok(rs);
	}

	@GetMapping("/task/{taskId}")
	public ResponseEntity<?> getTaskComment(@PathVariable String taskId) {
		List<CommentDTO> rs = commentService.getCommentsByTask(taskId);
		return ok(rs);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> findCommentById(@PathVariable String id) {
		CommentDTO rs = commentService.findById(id);
		return ok(rs);
	}

	@PostMapping()
	public ResponseEntity<?> createComment(@RequestBody CommentDTO comment, HttpServletRequest request) {

		CommentDTO rs = new CommentDTO();
		UserLoginDetailDTO curUser = getCurrentUser(request);
		try {
			rs = commentService.addComment(comment, curUser.getUserId());

		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			return error(ApiError.INTERNAL_SERVER_ERROR, request);
		}
		return ok(rs);

	}

	@PutMapping("/{commentId}")
	public ResponseEntity<?> updateComment(@RequestBody CommentDTO comment, @PathVariable String commentId)
			 {


		CommentDTO rs = new CommentDTO();
		try {
			rs = commentService.updateComment(comment.getBody(), commentId);

		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);

		}
		return ok(rs);

	}

	@DeleteMapping("/{commentId}")
	public ResponseEntity<?> deleteComment(@PathVariable String commentId) {

		try {
			commentService.deleteComment(commentId);

		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);

		}
		return ok(ApiStatus.OK);

	}

}
