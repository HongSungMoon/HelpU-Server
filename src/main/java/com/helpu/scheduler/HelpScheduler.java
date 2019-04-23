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
	
	public static ConcurrentHashMap<String, Integer> stopMap = new ConcurrentHashMap<>();

	@Scheduled(fixedDelay = 10000)
	public void helpScheduler() throws UnsupportedEncodingException, JSONException {

		Iterator<String> iterator = UserServiceImpl.helpMap.keySet().iterator();
		while (iterator.hasNext()) {

			String requester = iterator.next();
			String provider = UserServiceImpl.helpMap.get(requester);

			if (!UserServiceImpl.startMap.containsKey(requester)) {
				
				if(!stopMap.contains(requester)) {
					stopMap.put(requester, 0);
				} else {
					stopMap.put(requester, stopMap.get(requester) + 1);
				}
				
				if(stopMap.get(requester) > 1000) {
					stopMap.remove(requester);
					UserServiceImpl.helpMap.remove(requester);
					String requesterToken = helpuMapper.getTokenByIdx(requester);
					String providerToken = helpuMapper.getTokenByIdx(provider);
					pushService.sendInfo(requesterToken, "도움 제공자가 도움 제공에 실피해였습니다.");
					pushService.sendInfo(providerToken, "오랜 기간도움을 제공하지 않아 도움 제공에 실패하였습니다.");
					return;
				}

				String requesterLocation = UserServiceImpl.locationMap.get(requester);
				String providerLocation = UserServiceImpl.locationMap.get(provider);

				double distance = distanceUtil.calDistance(requesterLocation, providerLocation);

				System.out.println("[scheduler] distance = " + distance);

				if (distance < 50) {
					
					String requesterToken = helpuMapper.getTokenByIdx(requester);
					String providerToken = helpuMapper.getTokenByIdx(provider);
					
					pushService.sendInfo(requesterToken, "도움 받기가 성공적으로 끝났습니다.");
					pushService.helpComplete(providerToken);

					helpuMapper.addHelpCount(provider);
					
					int grade = helpuMapper.getGrade(provider);
					int point = helpuMapper.getPoint(provider);
					
					if(grade != 1 && (point + 10) % 100 == 0) {
						helpuMapper.updateGrade(provider);
					}

					UserServiceImpl.helpMap.remove(requester);
					System.out.println("[scheduler] Success!!!!!!!!!!!!1");

				}
			}

		}

	}

}
