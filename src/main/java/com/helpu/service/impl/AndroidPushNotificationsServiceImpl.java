package com.helpu.service.impl;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.helpu.interceptor.HeaderRequestInterceptor;
import com.helpu.service.AndroidPushNotificationsService;

@Service("pushService")
public class AndroidPushNotificationsServiceImpl implements AndroidPushNotificationsService {

	private static final String FIREBASE_SERVER_KEY = "AAAA52nGfu4:APA91bE4xIXacxY1VkytNT5y-iv52-703NOlyN_-5rnt7AKsoj4BzEPNrcTM8TZIWtbvRFGGUFp614Vn3NWbfVw9goetM6GDHw3u_EUOQFjtlnkzZXebXhAz9_tYPrVnMlvJutzN741b";
	private static final String FIREBASE_API_URL = "https://fcm.googleapis.com/fcm/send";

	@Override
	@Async
	public boolean send(String token, String msg) throws JSONException {
		
		JSONObject body = new JSONObject();
		JSONObject to = new JSONObject();
		JSONObject collapse_key = new JSONObject();
		JSONObject notification = new JSONObject();

		body.put("to", token);
		body.put("collapse_key", "type_a");
		
		notification.put("title", "HelpU");
		notification.put("body", msg);
		
		body.put("notification", notification);

		HttpEntity<String> request = new HttpEntity<>(body.toString());

		RestTemplate restTemplate = new RestTemplate();

		/**
		 * https://fcm.googleapis.com/fcm/send Content-Type:application/json
		 * Authorization:key=FIREBASE_SERVER_KEY
		 */

		ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + FIREBASE_SERVER_KEY));
		interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
		restTemplate.setInterceptors(interceptors);

		String firebaseResponse = restTemplate.postForObject(FIREBASE_API_URL, request, String.class);

		CompletableFuture<String> pushNotification = CompletableFuture.completedFuture(firebaseResponse);

		CompletableFuture.allOf(pushNotification).join();

		try {
			String response = pushNotification.get();
			return true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		return false;
	}

}
