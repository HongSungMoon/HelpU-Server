package com.helpu.scheduler;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.helpu.database.HelpUMapper;
import com.helpu.service.UserService;
import com.helpu.service.impl.AndroidPushNotificationsServiceImpl;
import com.helpu.service.impl.UserServiceImpl;
import com.helpu.utils.DistanceUtil;

@Component
public class HelpScheduler {
	
	@Autowired
	private HelpUMapper helpuMapper;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AndroidPushNotificationsServiceImpl pushService;
	
	public DistanceUtil distanceUtil = new DistanceUtil();
	
	@Scheduled(fixedDelay = 5000)
	public void helpScheduler() throws UnsupportedEncodingException, JSONException{
		
		Iterator<String> iterator = UserServiceImpl.helpMap.keySet().iterator();
	    while (iterator.hasNext()) {
	    	
	        String requester = iterator.next();
	        String provider = UserServiceImpl.helpMap.get(requester);
	        
	        String requesterLocation = UserServiceImpl.locationMap.get(requester);
	        String providerLocation = UserServiceImpl.locationMap.get(provider);
	        
	        double distance = distanceUtil.calDistance(requesterLocation, providerLocation);
	        
	       System.out.println("[scheduler] distance = " + distance);
	        
	        if(distance < 50) {
	        	
	        	String requesterToken = helpuMapper.getToken(requester);
	        	String providerToken = helpuMapper.getToken(provider);
	        	
//	        	pushService.helpComplete(requesterToken);
	        	pushService.helpComplete(providerToken);
	        	
	        	UserServiceImpl.helpMap.remove(requester);
	        	System.out.println("[scheduler] Success!!!!!!!!!!!!1");
	        }
	        
	    }
		
	}

}
