package com.datn.doffice.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.datn.doffice.entity.TaskAssignEntity;

@Repository
public class TaskAssignCollection {

	@Autowired
	private MongoTemplate mongoTemplate;

	public void insertObject(Object object) {

		TaskAssignEntity wa = (TaskAssignEntity) object;
		Criteria criteria = Criteria.where("task_id").is(wa.getTaskId()).and("user_id").is(wa.getUserId());
		Query query = new Query(criteria);
		TaskAssignEntity workAssignEntity = mongoTemplate.findOne(query, TaskAssignEntity.class);
		if (workAssignEntity == null) {
			mongoTemplate.insert(object);
		}

	}

	public void removeTaskAssign(String taskId, String userId) {
		Criteria criteria = Criteria.where("task_id").is(taskId).and("user_id").is(userId);
		Query query = new Query(criteria);
		mongoTemplate.remove(query, TaskAssignEntity.class);

	}

	public List<String> getTaskAssignByUserId(String userId) {
		Criteria criteria = Criteria.where("user_id").is(userId);
		Query query = new Query(criteria);
		List<TaskAssignEntity> taskAssignEntities = mongoTemplate.find(query, TaskAssignEntity.class);
		List<String> listTaskId = new ArrayList<String>();
		if (taskAssignEntities != null) {
			for (TaskAssignEntity taskAssignEntity : taskAssignEntities) {
				listTaskId.add(taskAssignEntity.getTaskId());
			}
			return listTaskId;
		}

		return null;

	}

	public List<TaskAssignEntity> getTaskAssignByTaskId(String taskId) {
		Criteria criteria = Criteria.where("task_id").is(taskId);
		Query query = new Query(criteria);
		return mongoTemplate.find(query, TaskAssignEntity.class);

	}

	public TaskAssignEntity updateOject(TaskAssignEntity taskAssignEntity) {
		return mongoTemplate.save(taskAssignEntity);
	}
	
	public void deleteTaskAssign(String taskId, String userId) {
		Criteria criteria = Criteria.where("task_id").is(taskId).and("user_id").is(userId);
		Query query = new Query(criteria);
		mongoTemplate.remove(query, TaskAssignEntity.class);

	}
}
