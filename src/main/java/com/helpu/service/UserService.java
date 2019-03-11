package com.helpu.service;

import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;

import com.helpu.model.request.GetProviderPhones;
import com.helpu.model.request.GetUserInfo;
import com.helpu.model.request.HelpAccept;
import com.helpu.model.request.HelpRegistration;
import com.helpu.model.request.ProviderRemove;
import com.helpu.model.request.SetAlarm;
import com.helpu.model.request.HelpRequest;
import com.helpu.model.request.Login;
import com.helpu.model.request.Logout;
import com.helpu.model.request.ProviderLocationRegistration;
import com.helpu.model.request.UserIdCheck;
import com.helpu.model.request.UserRegistration;
import com.helpu.model.request.UserUpdate;
import com.helpu.model.response.wrapper.ResponseWrapper;

public interface UserService {

	ResponseWrapper userRegistration(UserRegistration param);

	ResponseWrapper userLogin(Login param);

	ResponseWrapper idCheck(UserIdCheck param);

	ResponseWrapper helpRegistration(HelpRegistration param);

	ResponseWrapper helpRequest(HelpRequest param) throws JSONException;

	ResponseWrapper providerLocationRegistration(ProviderLocationRegistration param);

	ResponseWrapper providerRemove(ProviderRemove param);

	ResponseWrapper getProviderPhones(GetProviderPhones param);

	ResponseWrapper getUserInfo(GetUserInfo param);

	ResponseWrapper helpAccept(HelpAccept param);

	ResponseWrapper userUpdate(UserUpdate param);
	
	ConcurrentHashMap<String, String> getHelpMap();
	
	ConcurrentHashMap<String, String> getLocationMap();

	ResponseWrapper setAlarm(SetAlarm param);

	ResponseWrapper userLogout(Logout param);

}
