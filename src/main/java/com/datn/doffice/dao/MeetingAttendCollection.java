package com.datn.doffice.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.datn.doffice.entity.MeetingAttendEntity;


@Repository
public class MeetingAttendCollection {

	@Autowired
	private MongoTemplate mongoTemplate;

	public void insertObject(Object object) {
		mongoTemplate.insert(object);
	}

	public List<MeetingAttendEntity> getMeetingAttendByMeetingId(String meetingId) {

		Criteria criteria = Criteria.where("meeting_id").is(meetingId);
		Query query = new Query(criteria);
		return mongoTemplate.find(query, MeetingAttendEntity.class);

	}

	public List<MeetingAttendEntity> getMeetingAttendByUserId(String userId) {
		Criteria criteria = Criteria.where("user_id").is(userId);
		Query query = new Query(criteria);
		return mongoTemplate.find(query, MeetingAttendEntity.class);

	}
	
	public List<MeetingAttendEntity> getMeetingAttendMeetingId(String meetingId) {
		Criteria criteria = Criteria.where("meeting_jd").is(meetingId);
		Query query = new Query(criteria);
		return mongoTemplate.find(query, MeetingAttendEntity.class);

	}

	public MeetingAttendEntity updateOject(MeetingAttendEntity meetingAttendEntity) {
		return mongoTemplate.save(meetingAttendEntity);

	}

}
