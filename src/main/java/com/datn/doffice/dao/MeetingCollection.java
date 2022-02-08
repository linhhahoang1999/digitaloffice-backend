package com.datn.doffice.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.datn.doffice.entity.MeetingEntity;

@Repository
public class MeetingCollection {

	@Autowired
	private MongoTemplate mongoTemplate;

	public void insertObject(Object object) {
		mongoTemplate.insert(object);
	}

	// Tìm toàn bộ
	public List<MeetingEntity> findAll() {
		Criteria criteria = Criteria.where("is_deleted").is(false);
		Query query = new Query();
		query.with(Sort.by(Sort.Order.desc("start")));
		query = query.addCriteria(criteria);
		
		return mongoTemplate.find(query, MeetingEntity.class);
		

	}

	// tìm theo id
	public MeetingEntity findById(String id) {
		return mongoTemplate.findById(id, MeetingEntity.class);
	}

	// sửa thông tin meeting
	public MeetingEntity updateOject(MeetingEntity meetingEntity) {
		return mongoTemplate.save(meetingEntity);

	}

	// xoa cuoc hop
	public MeetingEntity deletedMeeting(String id) {
		Criteria criteria = Criteria.where("id").is(id).and("is_deleted").is(false);
		Query query = new Query(criteria);
		MeetingEntity meetingEntity = mongoTemplate.findOne(query, MeetingEntity.class);
		if (meetingEntity != null) {
			meetingEntity.setIsDeleted(true);
			return mongoTemplate.save(meetingEntity);
		}
		return null;

	}

}
