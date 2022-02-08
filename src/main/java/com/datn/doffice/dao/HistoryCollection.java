package com.datn.doffice.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.datn.doffice.entity.HistoryEntity;
import com.datn.doffice.entity.RecentlyEntity;
import com.datn.doffice.entity.WorkCommentEntity;

@Repository
public class HistoryCollection {

	@Autowired
	private MongoTemplate mongoTemplate;

	public void insertObject(Object object) {
		mongoTemplate.insert(object);
	}

	public List<HistoryEntity> getHistoryOfWork(String workId) {

		Query query = new Query();
		query.with(Sort.by(Sort.Order.desc("created_at")));
		query.addCriteria(Criteria.where("entity_id").is(workId).and("type").is("work"));
		return mongoTemplate.find(query, HistoryEntity.class);
	}

	public List<HistoryEntity> getHistoryOfTask(String taskId) {

		Query query = new Query();
		query.with(Sort.by(Sort.Order.desc("created_at")));
		query.addCriteria(Criteria.where("entity_id").is(taskId).and("type").is("task"));
		return mongoTemplate.find(query, HistoryEntity.class);
	}

	public HistoryEntity getHistoryById(String historyId) {
		return mongoTemplate.findById(historyId, HistoryEntity.class);
	}

	public HistoryEntity updateObject(HistoryEntity history) {
		return mongoTemplate.save(history);
	}

	public void deleteObject(HistoryEntity history) {
		mongoTemplate.remove(history);
	}

}
