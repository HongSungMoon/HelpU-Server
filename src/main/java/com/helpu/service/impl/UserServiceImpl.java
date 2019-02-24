package com.helpu.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.helpu.database.HelpUMapper;
import com.helpu.model.request.GetProviderPhones;
import com.helpu.model.request.HelpRegistration;
import com.helpu.model.request.HelpRequest;
import com.helpu.model.request.Login;
import com.helpu.model.request.ProviderLocationRegistration;
import com.helpu.model.request.ProviderRemove;
import com.helpu.model.request.UserIdCheck;
import com.helpu.model.request.UserRegistration;
import com.helpu.model.response.GetProviderPhonesParam;
import com.helpu.model.response.LoginResponse;
import com.helpu.model.response.wrapper.ResponseWrapper;
import com.helpu.service.UserService;
import com.helpu.utils.DistanceUtil;

@Service("userService")
public class UserServiceImpl implements UserService {
	
	@Autowired
	private AndroidPushNotificationsServiceImpl pushService;
	
	public ConcurrentHashMap<String, String> locationMap = new ConcurrentHashMap<>();
	public ConcurrentHashMap<String, String> helpMap = new ConcurrentHashMap<>();
	
	public DistanceUtil distanceUtil = new DistanceUtil();

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
	
	@Override
	public ResponseWrapper providerRemove(ProviderRemove param) {
		
		ResponseWrapper wrapper = createWrapper();
		
		helpuMapper.helpRemove(param);
		
		return wrapper;
	}

	@Override
	public ResponseWrapper helpRequest(HelpRequest param) throws JSONException {
		
		ResponseWrapper wrapper = createWrapper();
		
		locationMap.put("test2", "100,100");
		
		String requester = param.getRequester();
		List<String> providers = helpuMapper.getProviders(requester);
		
		if(providers.size() < 1) {
			wrapper.setResultCode(106);
			wrapper.setMessage("등록된 도움 제공자가 없습니다.");
			return wrapper;
		}
		
		List<Double> distances = new ArrayList<Double>();
		System.out.println("distances");
		for(int i=0; i<providers.size(); i++) {
			double distance = distanceUtil.calDistance(param.getLocation(), locationMap.get(providers.get(i)));
			System.out.println("provider : " + providers.get(i) + "  distance : " + distance);
			distances.add(distance);
		}
		
		String name = helpuMapper.getUserName(requester);
		Thread t = new Thread() {
		    public void run() {
		    	while(true) {
		    		if(distances.size() == 0) {
		    			String token = helpuMapper.getToken(requester);
		    			if(helpMap.containsKey(requester))
		    			helpMap.remove(requester);
		    			try {
							pushService.send(token, "도움 제공자 요청에 실패하였습니다.");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		    			break;
		    		}
		    		
		    		int minIdx = 0;
		    		double minDis = distances.get(0);
		    		for(int i=0; i<distances.size(); i++) {
		    			if(minDis >= distances.get(i))  {
		    				minIdx = i;
		    				minDis = distances.get(i);
		    			}
		    		}
		    		try {
		    		String token = helpuMapper.getToken(providers.get(minIdx));
						pushService.send(token, name + "님으로부터 도움 요청을 받았습니다.");
						Thread.sleep(30000);
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
		    		
		    		if(helpMap.get(requester) != null) {
		    			String token = helpuMapper.getToken(requester);
		    			String providerName = helpuMapper.getUserName(helpMap.get(requester));
		    			helpMap.remove(requester);
		    			try {
							pushService.send(token, providerName +"님으로부터 요청 승낙을 받았습니다.");
						} catch (JSONException e) {
							e.printStackTrace();
						}
		    			return;
		    		}
		    	}
		    }
		};
		t.start();

		return wrapper;
		
	}

	@Override
	public ResponseWrapper providerLocationRegistration(ProviderLocationRegistration param) {
		
		ResponseWrapper wrapper = createWrapper();
		System.out.println("provider : " + param.getProvider() + "   location : " + param.getLocation());
		locationMap.put(param.getProvider(), param.getLocation());
		
		return wrapper;
		
	}

	@Override
	public ResponseWrapper getProviderPhones(GetProviderPhones param) {
		
		ResponseWrapper wrapper = createWrapper();
		
		GetProviderPhonesParam resultParam = new GetProviderPhonesParam();
		List<String> phones = helpuMapper.getProviderPhones(param.getId());
		resultParam.setPhones(phones);
		
		wrapper.setParam(resultParam);
		
		return wrapper;
	}

}
