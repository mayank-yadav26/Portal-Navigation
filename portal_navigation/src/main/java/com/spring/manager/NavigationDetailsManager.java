package com.spring.manager;

import java.util.ArrayList;

import com.spring.model.NavigationDetails;

public interface NavigationDetailsManager {

	ArrayList<NavigationDetails> getNavigationDetailsList(String limit, String start);

	int getTotalEntires();

	void saveNavigationDetails(String baseUrl, String requestType, String parameters, String requestHeaders);

	void updateNavigationDetails(int navigationId, String baseUrl, String requestType, String parameters,
			String requestHeaders);

	void deleteNavigationDetails(String navigationIds);

	String runNavigationDetails(String navigationIds);

}
