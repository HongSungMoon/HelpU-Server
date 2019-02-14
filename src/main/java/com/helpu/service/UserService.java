package com.helpu.service;

import com.helpu.model.request.HelpRegistration;
import com.helpu.model.request.Login;
import com.helpu.model.request.UserIdCheck;
import com.helpu.model.request.UserRegistration;
import com.helpu.model.response.wrapper.ResponseWrapper;

public interface UserService {

	ResponseWrapper userRegistration(UserRegistration param);

	ResponseWrapper userLogin(Login param);

	ResponseWrapper idCheck(UserIdCheck param);

	ResponseWrapper helpRegistration(HelpRegistration param);

}
