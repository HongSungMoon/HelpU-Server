package com.helpu.database;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.helpu.model.request.Login;
import com.helpu.model.request.ProviderRemove;
import com.helpu.model.request.UserRegistration;
import com.helpu.model.request.UserUpdate;
import com.helpu.model.response.GetUserInfoParam;

@Mapper
public interface HelpUMapper {

	void userRegistration(UserRegistration param);
	
	String checkID(String id);

	String login(Login param);

	void helpRegistration(Map<String, Object> map);
	
	String getIdByPhone(String phone);

	String checkHelpRegistration(Map<String, Object> map);

	List<String> getProviders(String requester);

	String getToken(String id);

	String getUserName(String id);

	void helpRemove(ProviderRemove param);

	List<String> getProviderPhones(String id);

	GetUserInfoParam getUserInfo(String id);

	void updateUser(UserUpdate param);

	void addHelpCount(String provider);

}
