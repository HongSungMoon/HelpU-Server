package com.helpu.service;

import org.json.JSONException;

public interface AndroidPushNotificationsService {

	public boolean send(String token, String msg) throws JSONException;
	
}
