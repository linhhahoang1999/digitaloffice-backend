package com.datn.doffice.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.datn.doffice.entity.RecentlyEntity;


public interface RecentlyService {
	
    List<RecentlyEntity> getListRecently(String userId);
    
    void insertRecently(String userId, String entityId, String type,String title);
    
    void removeRecently(String recentlyId);
    

}
