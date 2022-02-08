package com.datn.doffice.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datn.doffice.dao.RecentlyCollection;
import com.datn.doffice.entity.RecentlyEntity;
import com.datn.doffice.service.RecentlyService;

@Service
public class RecentlyServiceImpl implements RecentlyService {

	@Autowired
	RecentlyCollection recentlyCollection;

	@Override
	public List<RecentlyEntity> getListRecently(String userId) {

		return recentlyCollection.getRecentlyOfUser(userId);
	}

	@Override
	public void insertRecently(String userId, String entityId, String type, String title) {
		RecentlyEntity recent = RecentlyEntity.builder()
				.createdAt(new Date())
				.entityId(entityId).type(type)
				.title(title).userId(userId).build();
		List<RecentlyEntity> listRecent = recentlyCollection.getRecentlyOfUser(userId);

		RecentlyEntity recentlyEntity = recentlyCollection.getRecent(userId, type, entityId);
		if (recentlyEntity != null) {
			recentlyEntity.setTitle(title);
			recentlyEntity.setCreatedAt(new Date());
			recentlyCollection.updateObject(recentlyEntity);

		}

		else {
			if (listRecent.size() < 5)
				recentlyCollection.insertObject(recent);
			else {
				RecentlyEntity newRecent = listRecent.get(4);
				newRecent.setType(type);
				newRecent.setTitle(title);
				newRecent.setEntityId(entityId);
				newRecent.setCreatedAt(new Date());
				recentlyCollection.updateObject(newRecent);
			}

		}

	}

	@Override
	public void removeRecently(String recentlyId) {
		RecentlyEntity recent = recentlyCollection.getRecentById(recentlyId);
		if (recent != null)
			recentlyCollection.deleteObject(recent);

	}

}
