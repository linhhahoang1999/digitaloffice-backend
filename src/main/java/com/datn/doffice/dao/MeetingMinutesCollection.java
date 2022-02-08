package com.datn.doffice.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.datn.doffice.entity.MeetingMinutesEntity;

@Repository
public class MeetingMinutesCollection {

	@Autowired
	private MongoTemplate mongoTemplate;

	public void insertObject(Object object) {
		mongoTemplate.insert(object);
	}

	public List<MeetingMinutesEntity> getAll() {

		Criteria criteria = Criteria.where("is_deleted").is(false);
		Query query = new Query(criteria);
		return mongoTemplate.find(query, MeetingMinutesEntity.class);

	}

	public MeetingMinutesEntity getMeetingMinutesByMeetingId(String meetingId) {

		Criteria criteria = Criteria.where("meeting_id").is(meetingId).and("is_deleted").is(false);
		Query query = new Query(criteria);
		return mongoTemplate.findOne(query, MeetingMinutesEntity.class);
	}

	public MeetingMinutesEntity getMeetingMinutesById(String meetingId) {

		Criteria criteria = Criteria.where("meeting_id").is(meetingId).and("is_deleted").is(false);
		Query query = new Query(criteria);
		return mongoTemplate.findOne(query, MeetingMinutesEntity.class);
	}

	public List<MeetingMinutesEntity> findById() {
		return mongoTemplate.findAll(MeetingMinutesEntity.class);
	}

	public MeetingMinutesEntity updateObject(MeetingMinutesEntity meetingMinutesEntity) {
		return mongoTemplate.save(meetingMinutesEntity);
	}

	public MeetingMinutesEntity deleteMeetingMinutes(String id) {

		Criteria criteria = Criteria.where("id").is(id).and("is_deleted").is(false);
		Query query = new Query(criteria);
		MeetingMinutesEntity meetingMinutesEntity = mongoTemplate.findOne(query, MeetingMinutesEntity.class);
		if (meetingMinutesEntity != null) {
			meetingMinutesEntity.setIsDeleted(true);
			return mongoTemplate.save(meetingMinutesEntity);
		}
		return null;
	}
}
