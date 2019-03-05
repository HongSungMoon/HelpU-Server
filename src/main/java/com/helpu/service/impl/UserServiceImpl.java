package com.helpu.service.impl;

import java.io.UnsupportedEncodingException;
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
import com.helpu.model.request.GetUserInfo;
import com.helpu.model.request.HelpAccept;
import com.helpu.model.request.HelpRegistration;
import com.helpu.model.request.HelpRequest;
import com.helpu.model.request.Login;
import com.helpu.model.request.ProviderLocationRegistration;
import com.helpu.model.request.ProviderRemove;
import com.helpu.model.request.UserIdCheck;
import com.helpu.model.request.UserRegistration;
import com.helpu.model.request.UserUpdate;
import com.helpu.model.response.GetProviderPhonesParam;
import com.helpu.model.response.GetUserInfoParam;
import com.helpu.model.response.LoginResponse;
import com.helpu.model.response.wrapper.ResponseWrapper;
import com.helpu.service.UserService;
import com.helpu.utils.DistanceUtil;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private AndroidPushNotificationsServiceImpl pushService;

	public static ConcurrentHashMap<String, String> locationMap = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<String, String> helpMap = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<String, Boolean> startMap = new ConcurrentHashMap<>();

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

		if (id != null) {

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

		if (id == null) {
			wrapper.setResultCode(102);
			wrapper.setMessage("존재하지 않는 사용자 ID입니다.");
			return wrapper;
		}

		String user_type = helpuMapper.login(param);

		if (user_type == null) {
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

		String id = helpuMapper.getIdByPhone(param.getProvider());

		if (id == null) {
			wrapper.setResultCode(104);
			wrapper.setMessage("가입되지 않은 제공자의 휴대폰 번호입니다.");
			return wrapper;
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("requester", param.getRequester());
		map.put("provider", id);

		String check = helpuMapper.checkHelpRegistration(map);

		if (check != null) {
			wrapper.setResultCode(110);
			wrapper.setMessage("이미 등록된 도움 제공자입니다.");
			return wrapper;
		}

		try {
			Map<String, Object> map2 = new HashMap<String, Object>();
			map2.put("requester", param.getRequester());
			map2.put("provider", id);
			helpuMapper.helpRegistration(map2);
		} catch (Exception e) {
			wrapper.setResultCode(105);
			wrapper.setMessage("사용자 ID가 옳바르지 않습니다.");
			return wrapper;
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

		locationMap.put("test6", "100,100");

		String requester = param.getRequester();
		List<String> providers = helpuMapper.getProviders(requester);
		
		

		if (providers.size() < 1) {
			wrapper.setResultCode(106);
			wrapper.setMessage("등록된 도움 제공자가 없습니다.");
			return wrapper;
		}
		
		locationMap.put(param.getRequester(), param.getLocation());

		List<Double> distances = new ArrayList<Double>();
		System.out.println("distances");
		for (int i = 0; i < providers.size(); i++) {
			double distance = distanceUtil.calDistance(param.getLocation(), locationMap.get(providers.get(i)));
//			System.out.println("provider : " + providers.get(i) + "  distance : " + distance);
			distances.add(distance);
		}

		if(startMap.containsKey(requester)) {
			wrapper.setResultCode(107);
			wrapper.setMessage("이미 도움 요청이 진행중입니다.");
			return wrapper;
		}
		
		startMap.put(requester, true);
		
		String name = helpuMapper.getUserName(requester);
		Thread t = new Thread() {
			public void run() {
				int num = param.getCount();
				if(num == 0)
					num = 1;
				for (int k = 0; k < num; k++) {
					while (true) {
						if (distances.size() == 0) {
							String token = helpuMapper.getToken(requester);
							if (!helpMap.containsKey(requester))
//								helpMap.remove(requester);
							try {
								pushService.sendInfo(token, "도움 제공자 요청에 실패하였습니다.");
							} catch (JSONException | UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							startMap.remove(requester);
							return;
						}

						int minIdx = 0;
						double minDis = distances.get(0);
						for (int i = 0; i < distances.size(); i++) {
							if (minDis >= distances.get(i)) {
								minIdx = i;
								minDis = distances.get(i);
							}
						}
						try {
							String token = helpuMapper.getToken(providers.get(minIdx));
							if (param.getMessage() == null) {
								pushService.send(token, name + "님으로부터 도움 요청을 받았습니다.", param.getLocation(), param.getRequester());
							} else {
								pushService.send(token, name + "님으로부터 도움 요청을 받았습니다. " + param.getMessage(),
										param.getLocation(), param.getRequester());
							}
							providers.remove(minIdx);
							distances.remove(minIdx);
							Thread.sleep(40000);
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						if (helpMap.get(requester) != null) {
							String token = helpuMapper.getToken(requester);
							String providerName = helpuMapper.getUserName(helpMap.get(requester));
//							helpMap.remove(requester);
							try {
								pushService.sendInfo(token, providerName + "님으로부터 요청 승낙을 받았습니다.");
							} catch (JSONException | UnsupportedEncodingException e) {
								e.printStackTrace();
							}
							startMap.remove(requester);
							return;
						}
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
		String preDistance = locationMap.get(param.getProvider());
		System.out.println("provider : " + param.getProvider() + "   location : " + param.getLocation());
		if(param.getLocation() != null)
			locationMap.put(param.getProvider(), param.getLocation());

		if (preDistance != null) {
			double distance = distanceUtil.calDistance(param.getLocation(), preDistance);
//			System.out.println(preDistance + " to " + param.getLocation() + "  : " + distance + " M");
		}

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

	@Override
	public ResponseWrapper getUserInfo(GetUserInfo param) {

		ResponseWrapper wrapper = createWrapper();

		GetUserInfoParam resultParam = new GetUserInfoParam();
		resultParam = helpuMapper.getUserInfo(param.getId());
		wrapper.setParam(resultParam);

		return wrapper;

	}

	@Override
	public ResponseWrapper helpAccept(HelpAccept param) {

		ResponseWrapper wrapper = createWrapper();
		
		if(helpMap.containsKey(param.getRequester())) {
			wrapper.setResultCode(100);
			wrapper.setMessage("이미 다른 도움 제공자가 수락하였습니다.");
		}

		helpMap.put(param.getRequester(), param.getProvider());

		return wrapper;

	}

	@Override
	public ResponseWrapper userUpdate(UserUpdate param) {

		ResponseWrapper wrapper = createWrapper();

		helpuMapper.updateUser(param);

		return wrapper;

	}
	
	public ConcurrentHashMap<String, String> getHelpMap() {
		return this.helpMap;
	}
	
	public ConcurrentHashMap<String, String> getLocationMap() {
		return this.locationMap;
	}

}
