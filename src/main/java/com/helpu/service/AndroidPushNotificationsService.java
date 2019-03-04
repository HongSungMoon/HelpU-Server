package com.helpu.service;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;

public interface AndroidPushNotificationsService {

	public boolean send(String token, String msg, String distance, String requester) throws JSONException, UnsupportedEncodingException;
	
	public boolean helpComplete(String token) throws JSONException, UnsupportedEncodingException;

	boolean sendInfo(String token, String msg)
			throws JSONException, UnsupportedEncodingException;

}
