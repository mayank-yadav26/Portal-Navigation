package com.spring.manager.impl;

import java.util.List;

import com.spring.dao.NavigationDetailsDao;
import com.spring.manager.NavigationDetailsManager;
import com.spring.model.NavigationDetails;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NavigationDetailsManagerImpl implements NavigationDetailsManager{
	NavigationDetailsDao navigationDetailsDao;
	
	@Override
	public List<NavigationDetails> getNavigationDetailsList(String limit, String start) {
		return navigationDetailsDao.getNavigationDetailsList(limit,start);
	}

	@Override
	public int  getTotalEntires() {
		return navigationDetailsDao.getTotalEntires();
	}
	
	@Override
	public void saveNavigationDetails(String navigationName,String baseUrl, String requestType, String parameters, String requestHeaders) {
		navigationDetailsDao.saveNavigationDetails(navigationName,baseUrl,requestType,parameters,requestHeaders);
	}
	
	@Override
	public void updateNavigationDetails(int navigationId, String navigationName, String baseUrl, String requestType, String parameters,
			String requestHeaders) {
		navigationDetailsDao.updateNavigationDetails(navigationId,navigationName,baseUrl,requestType,parameters,requestHeaders);
		
	}

	@Override
	public void deleteNavigationDetails(String navigationIds) {
		navigationDetailsDao.deleteNavigationDetails(navigationIds);
		
	}

	@Override
	public String runNavigationDetails(String navigationIds) {
		return navigationDetailsDao.runNavigationDetails(navigationIds);
	}
	
	@Override
	public String createNavigationFile(String navigationIds) {
		return navigationDetailsDao.createNavigationFile(navigationIds);
	}

}
