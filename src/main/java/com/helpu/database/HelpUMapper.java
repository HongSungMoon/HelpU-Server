package com.helpu.database;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.helpu.model.request.Login;
import com.helpu.model.request.UserRegistration;

@Mapper
public interface HelpUMapper {

	void userRegistration(UserRegistration param);
	
	String checkID(String id);

	String login(Login param);

	void helpRegistration(Map<String, Object> map);
	
	String getIdByPhone(String phone);

	String checkHelpRegistration(Map<String, Object> map);

}
