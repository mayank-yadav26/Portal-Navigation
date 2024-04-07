package com.spring.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.spring.manager.NavigationDetailsManager;
import com.spring.model.NavigationDetails;

public class NavigationDetailsAction {
	public static final Logger LOGGER = LogManager.getLogger(NavigationDetailsAction.class);
	private static NavigationDetailsManager navigationDetailsManager;
	private String totalCount;
	private String limit;
	private String start;
	private String data;
	private String total;
	private Map<String , Object> obj= new HashMap<String, Object>();
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
		Map<String , Object> temp = new HashMap<String, Object>();
		try {
			ApplicationContext context = new ClassPathXmlApplicationContext("spring/spring-config.xml");
			navigationDetailsManager=(NavigationDetailsManager) context.getBean("navigationDetailsManager");
			setTotalCount();
			ArrayList<NavigationDetails> navigationDetailsList = new ArrayList<NavigationDetails>();
			navigationDetailsList=navigationDetailsManager.getNavigationDetailsList(getLimit(),getStart());
			temp.put("data", navigationDetailsList);
			temp.put("total", totalCount);
		    obj = temp;
			//LOGGER.info("data is "+temp.get("data"));
			setSuccess(true);
		}     
		catch(Exception e){
			LOGGER.error("Error in getFilmData : "+e.getMessage());
			e.printStackTrace();
		}
		return "success";
	}
	
	public String addNavigationDetails() throws Exception {
		LOGGER.info("In addNavigationDetails Method");
		try {
			navigationDetailsManager.saveNavigationDetails(getNavigationName(),getBaseUrl(),getRequestType(),getParameters(),getRequestHeaders());
			obj.put("success", true);
		}     
		catch(Exception e){
			LOGGER.error("Error in addNavigationDetails : "+e.getMessage());
			e.printStackTrace();
		}
		return "success";
	}	
	
	public String editNavigationDetails() throws Exception {
		LOGGER.info("In editNavigationDetails Method");
		try {
			navigationDetailsManager.updateNavigationDetails(getNavigationId(),getNavigationName(),getBaseUrl(),getRequestType(),getParameters(),getRequestHeaders());
			obj.put("success", true);
		}     
		catch(Exception e){
			LOGGER.error("Error in editNavigationDetails : "+e.getMessage());
			e.printStackTrace();
		}
		return "success";
	}
	
	public String deleteNavigationDetails() throws Exception {
		LOGGER.info("In deleteNavigationDetails Method");
		try {
			navigationDetailsManager.deleteNavigationDetails(getNavigationIds());
			obj.put("success", true);
		}     
		catch(Exception e){
			LOGGER.error("Error in deleteNavigationDetails : "+e.getMessage());
			e.printStackTrace();
		}
		return "success";
	}
	
	public String runNavigationDetails() throws Exception {
		LOGGER.info("In runNavigationDetails Method");
		try {
			String docLink = navigationDetailsManager.runNavigationDetails(getNavigationIds());
			obj.put("success", true);
			obj.put("docLink", docLink);
		}     
		catch(Exception e){
			LOGGER.error("Error in runNavigationDetails : "+e.getMessage());
			e.printStackTrace();
		}
		return "success";
	}
	
	public String createNavigationFile() throws Exception {
		LOGGER.info("In createNavigationFile Method");
		try {
			String docLink = navigationDetailsManager.createNavigationFile(getNavigationIds());
			obj.put("success", true);
			obj.put("docLink", docLink);
		}     
		catch(Exception e){
			LOGGER.error("Error in createNavigationFile : "+e.getMessage());
			e.printStackTrace();
		}
		return "success";
	}
	
	private void setTotalCount() {
		try {		
			totalCount = navigationDetailsManager.getTotalEntires()+"";
		}catch(Exception e) {
			LOGGER.error("Error in setTotalCount : "+e.getMessage() + "\n" + e);
		}
	}
	
	
	public NavigationDetailsManager getNavigationDetailsManager() {
		return navigationDetailsManager;
	}

	public void setNavigationDetailsManager(NavigationDetailsManager navigationDetailsManager) {
		this.navigationDetailsManager = navigationDetailsManager;
	}

	public String getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

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
	public Map<String, Object> getObj() {
		return obj;
	}
	public void setObj(Map<String, Object> obj) {
		this.obj = obj;
	}

	public String getNavigationIds() {
		return navigationIds;
	}

	public void setNavigationIds(String navigationIds) {
		this.navigationIds = navigationIds;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getNavigationName() {
		return navigationName;
	}

	public void setNavigationName(String navigationName) {
		this.navigationName = navigationName;
	}
	
}
