package com.helpu.model.request;

public class HelpRequest {

	private String requester;
	private String location;
	private String message;
	private int count;
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

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
