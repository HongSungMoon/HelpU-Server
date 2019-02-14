package com.helpu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.helpu.model.request.HelpRegistration;
import com.helpu.model.request.Login;
import com.helpu.model.request.UserIdCheck;
import com.helpu.model.request.UserRegistration;
import com.helpu.model.response.wrapper.ResponseWrapper;
import com.helpu.service.UserService;

@Controller
@RequestMapping("/helpu") 
public class HelpUController {

	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/user/id/check", method = RequestMethod.POST)
	public ResponseEntity<ResponseWrapper> idCheck(@RequestBody UserIdCheck param) {
		
		ResponseWrapper response = userService.idCheck(param);
		
		return new ResponseEntity<ResponseWrapper>(response, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/user/registration", method = RequestMethod.POST)
	public ResponseEntity<ResponseWrapper> userRegistration(@RequestBody UserRegistration param) {
		
		ResponseWrapper response = userService.userRegistration(param);
		
		return new ResponseEntity<ResponseWrapper>(response, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/user/login", method = RequestMethod.POST)
	public ResponseEntity<ResponseWrapper> userLogin(@RequestBody Login param) {
		
		ResponseWrapper response = userService.userLogin(param);
		
		return new ResponseEntity<ResponseWrapper>(response, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/help/registration", method = RequestMethod.POST)
	public ResponseEntity<ResponseWrapper> helpRegistration(@RequestBody HelpRegistration param) {
		
		ResponseWrapper response = userService.helpRegistration(param);
		
		return new ResponseEntity<ResponseWrapper>(response, HttpStatus.OK);
		
	}
	
}
