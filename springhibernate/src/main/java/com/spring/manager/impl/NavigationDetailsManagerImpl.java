package com.spring.manager.impl;

import java.util.ArrayList;

import com.spring.dao.NavigationDetailsDao;
import com.spring.manager.NavigationDetailsManager;
import com.spring.model.NavigationDetails;

public class NavigationDetailsManagerImpl implements NavigationDetailsManager{
	NavigationDetailsDao navigationDetailsDao;
	
	@Override
	public ArrayList<NavigationDetails> getNavigationDetailsList(String limit, String start) {
		return navigationDetailsDao.getNavigationDetailsList(limit,start);
	}

	@Override
	public int  getTotalEntires() {
		return navigationDetailsDao.getTotalEntires();
	}
	
	@Override
	public void saveNavigationDetails(String baseUrl, String requestType, String parameters, String requestHeaders) {
		navigationDetailsDao.saveNavigationDetails(baseUrl,requestType,parameters,requestHeaders);
	}

	public NavigationDetailsDao getNavigationDetailsDao() {
		return navigationDetailsDao;
	}

	public void setNavigationDetailsDao(NavigationDetailsDao navigationDetailsDao) {
		this.navigationDetailsDao = navigationDetailsDao;
	}
}