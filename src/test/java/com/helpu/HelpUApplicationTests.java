package com.helpu;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.helpu.service.impl.AndroidPushNotificationsServiceImpl;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class HelpUApplicationTests {

	public AndroidPushNotificationsServiceImpl service = new AndroidPushNotificationsServiceImpl();
	
	String token = "frucUBH0Ua4:APA91bHPFGEpmTgdVXygJUT1lg3sdGGDq2GB77XnuoLKRQze3UpexbyFgo2Skv8jCw4v0ejoJP70ffVk_klBP-5N7SVoEYq9jkGidttlS7sRJLDdZ2Y8FD9p3nD4XUlXSGNSU2cYx04y";
	@Test
	public void contextLoads() {
		try {
			service.send(token,"한글");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

