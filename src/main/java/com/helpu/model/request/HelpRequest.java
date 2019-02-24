package com.helpu.model.request;

public class HelpRequest {

	private String requester;
	private String location;
		
	public String getRequester() {
		return requester;
	}
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setRequester(String requester) {
		this.requester = requester;
	}
	
}
