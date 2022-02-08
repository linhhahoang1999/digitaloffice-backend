package com.datn.doffice.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.datn.doffice.entity.WorkAssignEntity;
import com.datn.doffice.entity.WorkEntity;

@Repository
public class WorkAssignCollection {

	@Autowired
	private MongoTemplate mongoTemplate;

	public void insertObject(Object object) {
		WorkAssignEntity wa = (WorkAssignEntity) object;
		Criteria criteria = Criteria.where("work_id").is(wa.getWorkId()).and("user_id").is(wa.getUserId());
		Query query = new Query(criteria);
		WorkAssignEntity workAssignEntity = mongoTemplate.findOne(query, WorkAssignEntity.class);
		
		if (workAssignEntity == null) {
			 mongoTemplate.insert(object);
		}

			
	}

	public List<WorkAssignEntity> getWorkAssignByUserId(String userId) {

		Criteria criteria = Criteria.where("user_id").is(userId).and("is_deleted").is(false);
		Query query = new Query(criteria);
		return mongoTemplate.find(query, WorkAssignEntity.class);

	}

	public List<String> getWorkAssignByWorkId(String userId) {

		Criteria criteria = Criteria.where("work_id").is(userId);
		Query query = new Query(criteria);
		List<WorkAssignEntity> workAssignEntities = mongoTemplate.find(query, WorkAssignEntity.class);
		List<String> rs = new ArrayList<String>();
		for (WorkAssignEntity wa : workAssignEntities) {
			rs.add(wa.getUserId().replace("\"", ""));
		}

		return rs;

	}

	public WorkAssignEntity updateObject(WorkAssignEntity workAssignEntity) {
		return mongoTemplate.save(workAssignEntity);
	}

	public void deleteWorkAssign(String workId, String userId) {
		Criteria criteria = Criteria.where("work_id").is(workId).and("user_id").is(userId);
		Query query = new Query(criteria);
		mongoTemplate.remove(query, WorkAssignEntity.class);

	}

}
