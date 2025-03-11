package com.spring.model;

import lombok.Data;

@Data
public class NavigationDetails {
	private int navigationId;
	private String navigationName;
	private String baseUrl;
	private String requestType;
	private String parameters;
	private String requestHeaders;
}
