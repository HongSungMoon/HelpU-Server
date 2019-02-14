package com.helpu.model.request;

import java.util.List;

public class HelpRegistration {

	private String requester;
	private List<String> provider;

	public String getRequester() {
		return requester;
	}

	public void setRequestor(String requester) {
		this.requester = requester;
	}

	public List<String> getProvider() {
		return provider;
	}

	public void setProvider(List<String> provider) {
		this.provider = provider;
	}

	public void setRequester(String requester) {
		this.requester = requester;
	}


}
