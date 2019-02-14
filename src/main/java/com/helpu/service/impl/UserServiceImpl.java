package com.helpu.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.helpu.database.HelpUMapper;
import com.helpu.model.request.HelpRegistration;
import com.helpu.model.request.Login;
import com.helpu.model.request.UserIdCheck;
import com.helpu.model.request.UserRegistration;
import com.helpu.model.response.LoginResponse;
import com.helpu.model.response.wrapper.ResponseWrapper;
import com.helpu.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private HelpUMapper helpuMapper;

	public ResponseWrapper createWrapper() {

		ResponseWrapper response = new ResponseWrapper();
		response.setResultCode(0);
		response.setMessage("success");
		return response;

	}
	
	@Override
	public ResponseWrapper idCheck(UserIdCheck param) {
		
		ResponseWrapper wrapper = createWrapper();
		
		String id = helpuMapper.checkID(param.getId());
		
		if(id != null) {
			
			wrapper.setResultCode(100);
			wrapper.setMessage("이미 존재하는 사용자 ID입니다.");
			
			return wrapper;
		}
		
		return wrapper;
	}

	@Override
	public ResponseWrapper userRegistration(UserRegistration param) {

		ResponseWrapper wrapper = createWrapper();

		try {
			helpuMapper.userRegistration(param);
		} catch (Exception e) {
			wrapper.setResultCode(101);
			wrapper.setMessage("이미 등록된 휴대폰 번호입니다.");
		}

		return wrapper;
	}

	@Override
	public ResponseWrapper userLogin(Login param) {

		ResponseWrapper wrapper = createWrapper();

		String id = helpuMapper.checkID(param.getId());
		
		if(id == null) { 
			wrapper.setResultCode(102);
			wrapper.setMessage("존재하지 않는 사용자 ID입니다.");
			return wrapper;
		}
		
		String user_type = helpuMapper.login(param); 
		
		if(user_type == null) { 
			wrapper.setResultCode(103);
			wrapper.setMessage("Password가 일치하지 않습니다.");
			return wrapper;
		}
		
		LoginResponse response = new LoginResponse();
		response.setUser_type(user_type);
		wrapper.setParam(response);

		return wrapper;
	}

	@Override
	public ResponseWrapper helpRegistration(HelpRegistration param) {
		
		ResponseWrapper wrapper = createWrapper();
		List<String> ids = new ArrayList<String>();
		
		for(int i=0; i<param.getProvider().size(); i++) {
			
			String id = helpuMapper.getIdByPhone(param.getProvider().get(i)); 
		
			if(id == null) {
				wrapper.setResultCode(104);
				wrapper.setMessage("가입되지 않은 제공자의 휴대폰 번호가 포함되어 있습니다.");
				return wrapper;
			}
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("requester", param.getRequester());
			map.put("provider",id);
			
			String check = helpuMapper.checkHelpRegistration(map);
			
			if(check == null) 
				ids.add(id);
		}
		
		for(int i=0; i<ids.size(); i++) {
			
			try {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("requester", param.getRequester());
				map.put("provider",ids.get(i));
				helpuMapper.helpRegistration(map);
			} catch(Exception e) {
				wrapper.setResultCode(105);
				wrapper.setMessage("사용자 ID가 옳바르지 않습니다.");
				return wrapper;
			}
			
		}
		
		return wrapper;
		
	}

}
