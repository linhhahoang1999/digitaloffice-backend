package com.datn.doffice.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Repository;

import com.datn.doffice.entity.WorkAssignEntity;
import com.datn.doffice.entity.WorkEntity;

@Repository
public class WorkCollection {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private MongoOperations mongoOperations;

	public void insertObject(Object object) {
		mongoTemplate.insert(object);
	}

	public WorkEntity findById(String id) {

		Criteria criteria = Criteria.where("id").is(id).and("is_deleted").is(false);
		Query query = new Query(criteria);

		WorkEntity work = mongoTemplate.findOne(query, WorkEntity.class);
		if (work != null)
			return work;
		else {
			System.err.println("Không có công việc này hoặc đã được xoá/lưu trữ");
			return null;
		}

	}

	public List<WorkEntity> fullTextSearch(String searchPhrase) {

		Criteria c1 = Criteria.where("title").regex(searchPhrase, "i").and("is_deleted").is(false)
				.and("is_stored").is(false);;
		Criteria c2 = Criteria.where("description").regex(searchPhrase, "i").and("is_deleted").is(false)
				.and("is_stored").is(false);
		Criteria criteria = new Criteria();
		criteria.orOperator(c1, c2);
		Query query = new Query(criteria);

		List<WorkEntity> rs = mongoTemplate.find(query, WorkEntity.class);

		return rs;
	}

	public List<WorkEntity> fullTextSearchStoredWork(String searchPhrase) {

		Criteria c1 = Criteria.where("title").regex(searchPhrase, "i");
		Criteria c2 = Criteria.where("description").regex(searchPhrase, "i").and("is_deleted").is(false)
				.and("is_stored").is(true);
		Criteria criteria = new Criteria();
		criteria.orOperator(c1, c2);
		Query query = new Query(criteria);

		List<WorkEntity> rs = mongoTemplate.find(query, WorkEntity.class);

		return rs;
	}

	public List<WorkEntity> findAll() {
		Query query = new Query();
		query.with(Sort.by(Sort.Order.desc("created_at")));
		Criteria criteria = Criteria.where("is_deleted").is(false).and("is_stored").is(false);
		query.addCriteria(criteria);
		return mongoTemplate.find(query, WorkEntity.class);

	}

	public List<WorkEntity> findAllStoredWorks() {
		Query query = new Query();
		query.with(Sort.by(Sort.Order.desc("created_at")));
		Criteria criteria = Criteria.where("is_deleted").is(false).and("is_stored").is(true);
		query.addCriteria(criteria);
		return mongoTemplate.find(query, WorkEntity.class);

	}

	public List<WorkEntity> findWorkByUser(String userId) {

		Criteria criteria = Criteria.where("user_id").is(userId);
		Query query = new Query(criteria);
		List<WorkAssignEntity> workAssgins = mongoTemplate.find(query, WorkAssignEntity.class);
		List<String> listWorkId = new ArrayList<>();
		for (WorkAssignEntity work : workAssgins)
			listWorkId.add(work.getWorkId());

		criteria = Criteria.where("id").in(listWorkId).and("is_deleted").is(false).and("is_stored").is(false);
		query = new Query();
		query.with(Sort.by(Sort.Order.desc("created_at")));
		query.addCriteria(criteria);
		return mongoTemplate.find(query, WorkEntity.class);

	}

	public WorkEntity updateObject(WorkEntity work) {
		return mongoTemplate.save(work);
	}

	public WorkEntity deleteWork(String id) {

		Criteria criteria = Criteria.where("id").is(id).and("is_deleted").is(false);
		Query query = new Query(criteria);
		WorkEntity work = mongoTemplate.findOne(query, WorkEntity.class);
		if (work != null) {
			work.setIsDeleted(true);
			return mongoTemplate.save(work);
		}
		return null;

	}

}
