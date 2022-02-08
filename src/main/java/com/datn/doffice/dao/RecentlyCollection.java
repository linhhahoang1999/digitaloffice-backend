package com.datn.doffice.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.datn.doffice.entity.RecentlyEntity;
import com.datn.doffice.entity.WorkCommentEntity;

@Repository
public class RecentlyCollection {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	public void insertObject(Object object) {
		mongoTemplate.insert(object);
	}
	
	
	public List<RecentlyEntity> getRecentlyOfUser( String userId){
		
		Query query = new Query();
		query.with(Sort.by(Sort.Order.desc("created_at")));
		query.addCriteria(Criteria.where("user_id").is(userId));
		return mongoTemplate.find(query, RecentlyEntity.class);
				
		}
	
	public RecentlyEntity getRecentById(String recentId) {
		
		return mongoTemplate.findById(recentId, RecentlyEntity.class);
		
	}
	
	public RecentlyEntity getRecent(String userId, String type, String entityId) {
		
		Criteria criteria = Criteria.where("user_id").is(userId).and("entity_id").is(entityId).and("type").is(type);
		Query query = new Query(criteria);
		return mongoTemplate.findOne(query, RecentlyEntity.class);
		
	}
	
	public RecentlyEntity updateObject(RecentlyEntity recently) {
		return mongoTemplate.save(recently);
	}
	
	public void deleteObject(RecentlyEntity recently) {
		mongoTemplate.remove(recently);
		
	}
	
	
}
