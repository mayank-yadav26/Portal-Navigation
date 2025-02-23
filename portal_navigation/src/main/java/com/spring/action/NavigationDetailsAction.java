package com.spring.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.spring.manager.NavigationDetailsManager;
import com.spring.model.NavigationDetails;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NavigationDetailsAction {
	public static final Logger LOGGER = LogManager.getLogger(NavigationDetailsAction.class);
	private NavigationDetailsManager navigationDetailsManager;
	private String totalCount;
	private String limit;
	private String start;
	private String data;
	private String total;
	private Map<String , Object> rootObj= new HashMap<String, Object>();
	private boolean success;
	
	private int navigationId;
	private String navigationIds;
	private String navigationName;
	private String baseUrl;
	private String requestType;
	private String parameters;
	private String requestHeaders;
	public String getNavigationDetails() {
		LOGGER.info("Inside getNavigationDetails");
		try {
			setTotalCount();
			List<NavigationDetails> navigationDetailsList = new ArrayList<NavigationDetails>();
			navigationDetailsList=navigationDetailsManager.getNavigationDetailsList(getLimit(),getStart());
			rootObj.put("data", navigationDetailsList);
			rootObj.put("total", totalCount);
			setSuccess(true);
		}     
		catch(Exception exp){
			LOGGER.error("Error in getNavigationDetails : ",exp);
		}
		return "success";
	}
	
	public String addNavigationDetails() throws Exception {
		LOGGER.info("In addNavigationDetails Method");
		try {
			navigationDetailsManager.saveNavigationDetails(getNavigationName(),getBaseUrl(),getRequestType(),getParameters(),getRequestHeaders());
			rootObj.put("success", true);
		}     
		catch(Exception exp){
			LOGGER.error("Error in addNavigationDetails : ",exp);
		}
		return "success";
	}	
	
	public String editNavigationDetails() throws Exception {
		LOGGER.info("In editNavigationDetails Method");
		try {
			navigationDetailsManager.updateNavigationDetails(getNavigationId(),getNavigationName(),getBaseUrl(),getRequestType(),getParameters(),getRequestHeaders());
			rootObj.put("success", true);
		}     
		catch(Exception exp){
			LOGGER.error("Error in editNavigationDetails : ",exp);
		}
		return "success";
	}
	
	public String deleteNavigationDetails() throws Exception {
		LOGGER.info("In deleteNavigationDetails Method");
		try {
			navigationDetailsManager.deleteNavigationDetails(getNavigationIds());
			rootObj.put("success", true);
		}     
		catch(Exception exp){
			LOGGER.error("Error in deleteNavigationDetails : ",exp);
		}
		return "success";
	}
	
	public String runNavigationDetails() throws Exception {
		LOGGER.info("In runNavigationDetails Method");
		try {
			String docLink = navigationDetailsManager.runNavigationDetails(getNavigationIds());
			rootObj.put("success", true);
			rootObj.put("docLink", docLink);
		}     
		catch(Exception exp){
			LOGGER.error("Error in runNavigationDetails : ",exp);
		}
		return "success";
	}
	
	public String createNavigationFile() throws Exception {
		LOGGER.info("In createNavigationFile Method");
		try {
			String docLink = navigationDetailsManager.createNavigationFile(getNavigationIds());
			rootObj.put("success", true);
			rootObj.put("docLink", docLink);
		}     
		catch(Exception exp){
			LOGGER.error("Error in createNavigationFile : ",exp);
		}
		return "success";
	}
	
	private void setTotalCount() {
		try {		
			totalCount = navigationDetailsManager.getTotalEntires()+"";
		}catch(Exception exp) {
			LOGGER.error("Error in setTotalCount : ",exp);
		}
	}
}
