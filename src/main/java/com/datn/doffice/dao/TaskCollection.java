package com.datn.doffice.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators.Filter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Repository;

import com.datn.doffice.entity.TaskAssignEntity;
import com.datn.doffice.entity.TaskEntity;
import com.datn.doffice.entity.WorkEntity;

@Repository
public class TaskCollection {

	@Autowired
	private MongoTemplate mongoTemplate;

	public void insertObject(Object object) {
		mongoTemplate.insert(object);
	}

	// Tim theo id
	public TaskEntity findById(String id) {
		return mongoTemplate.findById(id, TaskEntity.class);
	}

	public List<TaskEntity> searchTask(String searchPhrase) {

		Criteria c1 = Criteria.where("title").regex(searchPhrase, "i");
		Criteria c2 = Criteria.where("description").regex(searchPhrase, "i").and("is_deleted").is(false)
				.and("is_stored").is(false);
		Criteria criteria = new Criteria();
		criteria.orOperator(c1, c2);
		Query query = new Query(criteria);

		List<TaskEntity> tmp = mongoTemplate.find(query, TaskEntity.class);
		List<TaskEntity> rs = new ArrayList<TaskEntity>();
		
		for (TaskEntity task : tmp) {
			Criteria cri = Criteria.where("id").is(task.getWorkId()).and("is_deleted").is(false).and("is_stored")
					.is(false);
			Query query1 = new Query(cri);
			WorkEntity w = mongoTemplate.findOne(query1, WorkEntity.class);
			if (w != null)
				rs.add(task);
		}

		return rs;

	
	}

	public List<TaskEntity> getTaskByWorkId(String workId) {
		Criteria criteria = Criteria.where("work_id").is(workId).and("is_deleted").is(false);
		Query query = new Query(criteria);
		return mongoTemplate.find(query, TaskEntity.class);

	}

	public List<TaskEntity> findAll() {

		Criteria criteria = Criteria.where("is_deleted").is(false);
		Query query = new Query();
		query.with(Sort.by(Sort.Order.desc("created_at")));
		query.addCriteria(criteria);
		List<TaskEntity> tmp = mongoTemplate.find(query, TaskEntity.class);

		List<TaskEntity> rs = new ArrayList<TaskEntity>();

		for (TaskEntity task : tmp) {
			Criteria cri = Criteria.where("id").is(task.getWorkId()).and("is_deleted").is(false).and("is_stored")
					.is(false);
			Query query1 = new Query(cri);
			WorkEntity w = mongoTemplate.findOne(query1, WorkEntity.class);
			if (w != null)
				rs.add(task);
		}

		return rs;

	}

	public List<TaskEntity> findByUser(String userId) {
		List<TaskEntity> rs = new ArrayList<TaskEntity>();
		Criteria criteria = Criteria.where("user_id").is(userId);
		Query query = new Query(criteria);
		List<TaskAssignEntity> taskAssigns = mongoTemplate.find(query, TaskAssignEntity.class);

		List<String> listTaskId = new ArrayList<String>();
		if (taskAssigns != null) {
			for (TaskAssignEntity task : taskAssigns)
				listTaskId.add(task.getTaskId());

			Criteria c2 = Criteria.where("id").in(listTaskId).and("is_deleted").is(false);
			Query q2 = new Query();
			q2.with(Sort.by(Sort.Order.desc("created_at")));
			q2.addCriteria(c2);			
			List<TaskEntity> tmp = mongoTemplate.find(q2, TaskEntity.class);

			for (TaskEntity task : tmp) {
				Criteria c3 = Criteria.where("id").is(task.getWorkId()).and("is_deleted").is(false).and("is_stored")
						.is(false);
				Query q3 = new Query(c3);
				WorkEntity w = mongoTemplate.findOne(q3, WorkEntity.class);
				if (w != null)
					rs.add(task);
			}

			return rs;

		}
		return null;

	}

	public TaskEntity updateOject(TaskEntity taskEntity) {
		return mongoTemplate.save(taskEntity);

	}

	public TaskEntity deleteTask(String id) {

		Criteria criteria = Criteria.where("id").is(id).and("is_deleted").is(false);
		Query query = new Query(criteria);
		TaskEntity taskEntity = mongoTemplate.findOne(query, TaskEntity.class);
		if (taskEntity != null) {
			taskEntity.setIsDeleted(true);
			return mongoTemplate.save(taskEntity);
		}
		return null;
	}

}
