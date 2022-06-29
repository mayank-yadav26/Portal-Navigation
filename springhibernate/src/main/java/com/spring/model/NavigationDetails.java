package com.spring.model;

public class NavigationDetails {
	private int navigationId;
	private String baseUrl;
	private String requestType;
	private String parameters;
	private String requestHeaders;
	
	public int getNavigationId() {
		return navigationId;
	}
	public void setNavigationId(int navigationId) {
		this.navigationId = navigationId;
	}
	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getParameters() {
		return parameters;
	}
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
	public String getRequestHeaders() {
		return requestHeaders;
	}
	public void setRequestHeaders(String requestHeaders) {
		this.requestHeaders = requestHeaders;
	}
	
}
