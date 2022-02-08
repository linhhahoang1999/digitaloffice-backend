package com.datn.doffice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.datn.doffice.annotation.Api;
import com.datn.doffice.entity.RecentlyEntity;
import com.datn.doffice.enums.ApiStatus;
import com.datn.doffice.service.RecentlyService;

@Api(path = "/api/recently")
public class RecentlyController extends ApiController {
	
	@Autowired
	RecentlyService recentlyService;

	@GetMapping("/{userId}")
	public ResponseEntity<?> getRecently(@PathVariable String userId) {
		List<RecentlyEntity> rs = recentlyService.getListRecently(userId);
		return ok(rs);
	}

	@PostMapping()
	public ResponseEntity<?> insertRecently(@RequestBody RecentlyEntity recent) {
		
		recentlyService.insertRecently(recent.getUserId(), recent.getEntityId(), recent.getType(),recent.getTitle());
		return ok(ApiStatus.OK);
	}
	
	@DeleteMapping("/{recentlyId}")
	public ResponseEntity<?> removeRecently(@PathVariable String recentlyId){
		recentlyService.removeRecently(recentlyId);
		return ok(ApiStatus.OK);
	}
}
