package com.helpu.controller;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.helpu.model.request.GetProviderPhones;
import com.helpu.model.request.GetUserInfo;
import com.helpu.model.request.HelpAccept;
import com.helpu.model.request.HelpRegistration;
import com.helpu.model.request.HelpRequest;
import com.helpu.model.request.Login;
import com.helpu.model.request.Logout;
import com.helpu.model.request.ProviderLocationRegistration;
import com.helpu.model.request.ProviderRemove;
import com.helpu.model.request.SetAlarm;
import com.helpu.model.request.UserIdCheck;
import com.helpu.model.request.UserRegistration;
import com.helpu.model.request.UserUpdate;
import com.helpu.model.response.wrapper.ResponseWrapper;
import com.helpu.service.UserService;

@Controller
@RequestMapping("/helpu") 
public class HelpUController {

	@Autowired
	private UserService userService;
	
	/*
	 * id 중복체크 확인 API
	 * */
	@RequestMapping(value = "/user/id/check", method = RequestMethod.POST)
	public ResponseEntity<ResponseWrapper> idCheck(@RequestBody UserIdCheck param) {
		
		ResponseWrapper response = userService.idCheck(param);
		
		return new ResponseEntity<ResponseWrapper>(response, HttpStatus.OK);
		
	}
	
	/*
	 * 회원 가입 API
	 * */
	@RequestMapping(value = "/user/registration", method = RequestMethod.POST)
	public ResponseEntity<ResponseWrapper> userRegistration(@RequestBody UserRegistration param) {
		
		ResponseWrapper response = userService.userRegistration(param);
		
		return new ResponseEntity<ResponseWrapper>(response, HttpStatus.OK);
		
	}
	
	/*
	 * 로그인 API
	 * */
	@RequestMapping(value = "/user/login", method = RequestMethod.POST)
	public ResponseEntity<ResponseWrapper> userLogin(@RequestBody Login param) {
		
		ResponseWrapper response = userService.userLogin(param);
		
		return new ResponseEntity<ResponseWrapper>(response, HttpStatus.OK);
		
	}
	
	/*
	 * 로그아웃 API
	 * */
	@RequestMapping(value = "/user/logout", method = RequestMethod.POST)
	public ResponseEntity<ResponseWrapper> userLogout(@RequestBody Logout param) {
		
		ResponseWrapper response = userService.userLogout(param);
		
		return new ResponseEntity<ResponseWrapper>(response, HttpStatus.OK);
		
	}
	
	/*
	 * 회원정보 가져오는 API
	 * */
	@RequestMapping(value = "/user/info", method = RequestMethod.POST)
	public ResponseEntity<ResponseWrapper> getUserInfo(@RequestBody GetUserInfo param) {
		
		ResponseWrapper response = userService.getUserInfo(param);
		
		return new ResponseEntity<ResponseWrapper>(response, HttpStatus.OK);
		
	}
	
	/*
	 * 회원정보 업데이트 API
	 * */
	@RequestMapping(value = "/user/update", method = RequestMethod.POST)
	public ResponseEntity<ResponseWrapper> userUpdate(@RequestBody UserUpdate param) {
		
		ResponseWrapper response = userService.userUpdate(param);
		
		return new ResponseEntity<ResponseWrapper>(response, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/provider/phones", method = RequestMethod.POST)
	public ResponseEntity<ResponseWrapper> getProviderPhones(@RequestBody GetProviderPhones param) {
		
		ResponseWrapper response = userService.getProviderPhones(param);
		
		return new ResponseEntity<ResponseWrapper>(response, HttpStatus.OK);
		
	}
	
	/*
	 * 도움제공자 등록 API
	 * */
	@RequestMapping(value = "/help/registration", method = RequestMethod.POST)
	public ResponseEntity<ResponseWrapper> helpRegistration(@RequestBody HelpRegistration param) {
		
		ResponseWrapper response = userService.helpRegistration(param);
		
		return new ResponseEntity<ResponseWrapper>(response, HttpStatus.OK);
		
	}
	
	/*
	 * 도움제공자 삭제 API
	 * */
	@RequestMapping(value = "/help/remove", method = RequestMethod.POST)
	public ResponseEntity<ResponseWrapper> providerRemove(@RequestBody ProviderRemove param) {
		
		ResponseWrapper response = userService.providerRemove(param);
		
		return new ResponseEntity<ResponseWrapper>(response, HttpStatus.OK);
		
	}
	
	/*
	 * 도움 요청 API
	 * */
	@RequestMapping(value = "/help/request", method = RequestMethod.POST)
	public ResponseEntity<ResponseWrapper> helpRequest(@RequestBody HelpRequest param) throws JSONException {
		
		ResponseWrapper response = userService.helpRequest(param);
		
		return new ResponseEntity<ResponseWrapper>(response, HttpStatus.OK);
		
	}
	
	/*
	 * 도움 제공자 위치 등록 API
	 * */
	@RequestMapping(value = "/provider/location", method = RequestMethod.POST)
	public ResponseEntity<ResponseWrapper> providerLocationRegistratin(@RequestBody ProviderLocationRegistration param) {
		
		ResponseWrapper response = userService.providerLocationRegistration(param);
		
		return new ResponseEntity<ResponseWrapper>(response, HttpStatus.OK);
		
	}

	/*
	 * 도움 수락 API
	 * */
	@RequestMapping(value = "/help/accept", method = RequestMethod.POST)
	public ResponseEntity<ResponseWrapper> helpAccept(@RequestBody HelpAccept param) {
		
		ResponseWrapper response = userService.helpAccept(param);
		
		return new ResponseEntity<ResponseWrapper>(response, HttpStatus.OK);
		
	}
	
	/*
	 * 알람 설정 API
	 * */
	@RequestMapping(value = "/user/alarm", method = RequestMethod.POST)
	public ResponseEntity<ResponseWrapper> setAlarm(@RequestBody SetAlarm param) {
		
		ResponseWrapper response = userService.setAlarm(param);
		
		return new ResponseEntity<ResponseWrapper>(response, HttpStatus.OK);
		
	}
	
}
