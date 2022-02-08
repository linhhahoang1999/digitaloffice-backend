package com.datn.doffice.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.datn.doffice.entity.TaskEntity;
import com.datn.doffice.entity.WorkCommentEntity;

@Repository
public class WorkCommentCollection {

	@Autowired
	private MongoTemplate mongoTemplate;

	public void insertObject(Object object) {
		mongoTemplate.insert(object);
	
	}

	public List<WorkCommentEntity> getListWorkComment(String workId) {
		Query query = new Query();
		query.with(Sort.by(Sort.Order.desc("created_at")));
		query.addCriteria(Criteria.where("type").is("work").and("entity_id").is(workId).and("is_deleted").is(false));

		return mongoTemplate.find(query, WorkCommentEntity.class);
	}

	public List<WorkCommentEntity> getListTaskComment(String taskId) {
		Query query = new Query();
		query.with(Sort.by(Sort.Order.desc("created_at")));
		query.addCriteria(Criteria.where("type").is("task").and("entity_id").is(taskId).and("is_deleted").is(false));

		return mongoTemplate.find(query, WorkCommentEntity.class);
	}

	public void deleteComment(String commentId) {
		Query query = Query.query(Criteria.where("id").is(commentId));
		Update update = new Update();
		update.set("is_deleted", true);
		mongoTemplate.updateFirst(query, update, WorkCommentEntity.class);
	}

	public WorkCommentEntity updateOject(WorkCommentEntity comment) {
		return mongoTemplate.save(comment);

	}
	
	public WorkCommentEntity findById(String commentId) {
		Criteria criteria = Criteria.where("id").is(commentId).and("is_deleted").is(false);
		Query query = new Query(criteria);
		return mongoTemplate.findOne(query, WorkCommentEntity.class);

	}

}