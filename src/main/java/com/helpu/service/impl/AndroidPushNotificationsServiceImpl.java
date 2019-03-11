package com.helpu.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
	public boolean send(String token, String msg, String location, String requester) throws JSONException, UnsupportedEncodingException {

		JSONObject body = new JSONObject();
		JSONObject notification = new JSONObject();
		JSONObject data = new JSONObject();

		body.put("to", token);
		body.put("collapse_key", "type_a");

		notification.put("title", "HelpU");
		notification.put("body", msg);
		notification.put("location", location);
		notification.put("click_action", "OPEN_ACTIVITY_1");
		data.put("location", location);
		data.put("title", "HelpU");
		data.put("message", msg);
		data.put("requester", requester);
		body.put("notification", notification);
		body.put("data", data);
		
		HttpHeaders headers = new HttpHeaders();
		Charset utf8 = Charset.forName("UTF-8");
		MediaType mediaType = new MediaType("application", "json", utf8);
		headers.setContentType(mediaType);
		
		System.out.println(body.toString());
		HttpEntity<String> request = new HttpEntity<>(body.toString(), headers);

		RestTemplate restTemplate = new RestTemplate();

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
	
	@Override
	@Async
	public boolean sendInfo(String token, String msg) throws JSONException, UnsupportedEncodingException {

		JSONObject body = new JSONObject();
		JSONObject notification = new JSONObject();

		body.put("to", token);
		body.put("collapse_key", "type_a");

		notification.put("title", "HelpU");
		notification.put("body", msg);
		notification.put("click_action", "OPEN_ACTIVITY_2");
		body.put("notification", notification);
		
		
		HttpHeaders headers = new HttpHeaders();
		Charset utf8 = Charset.forName("UTF-8");
		MediaType mediaType = new MediaType("application", "json", utf8);
		headers.setContentType(mediaType);
		
		System.out.println(body.toString());
		HttpEntity<String> request = new HttpEntity<>(body.toString(), headers);

		RestTemplate restTemplate = new RestTemplate();

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
	
	@Override
	@Async
	public boolean helpComplete(String token) throws JSONException, UnsupportedEncodingException {

		JSONObject body = new JSONObject();
		JSONObject notification = new JSONObject();
		JSONObject data = new JSONObject();

		body.put("to", token);
		body.put("collapse_key", "type_a");

		notification.put("title", "HelpU");
		notification.put("body", "도움 제공을 완료하였습니다.");
		notification.put("click_action", "OPEN_ACTIVITY_1");
		data.put("title", "HelpU");
		body.put("notification", notification);
		body.put("data", data);
		
		
		HttpHeaders headers = new HttpHeaders();
		Charset utf8 = Charset.forName("UTF-8");
		MediaType mediaType = new MediaType("application", "json", utf8);
		headers.setContentType(mediaType);
		
		System.out.println(body.toString());
		HttpEntity<String> request = new HttpEntity<>(body.toString(), headers);

		RestTemplate restTemplate = new RestTemplate();

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
