package com.spring.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.spring.manager.NavigationDetailsManager;
import com.spring.model.NavigationDetails;

public class NavigationDetailsAction {
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
	private String baseUrl;
	private String requestType;
	private String parameters;
	private String requestHeaders;
	
	public String getNavigationDetails() {
		System.out.println("Inside getNavigationDetails");
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
			//System.out.println("data is "+temp.get("data"));
			setSuccess(true);
		}     
		catch(Exception e){
			System.out.println("Error in getFilmData : "+e.getMessage());
			e.printStackTrace();
		}
		return "success";
	}
	
	public String addNavigationDetails() throws Exception {
		System.out.println("In addNavigationDetails Method");
		try {
			navigationDetailsManager.saveNavigationDetails(getBaseUrl(),getRequestType(),getParameters(),getRequestHeaders());
			obj.put("success", true);
		}     
		catch(Exception e){
			System.out.println("Error in addNavigationDetails : "+e.getMessage());
			e.printStackTrace();
		}
		return "success";
	}	
	
	public String editNavigationDetails() throws Exception {
		System.out.println("In editNavigationDetails Method");
		try {
			navigationDetailsManager.updateNavigationDetails(getNavigationId(),getBaseUrl(),getRequestType(),getParameters(),getRequestHeaders());
			obj.put("success", true);
		}     
		catch(Exception e){
			System.out.println("Error in editNavigationDetails : "+e.getMessage());
			e.printStackTrace();
		}
		return "success";
	}
	
	public String deleteNavigationDetails() throws Exception {
		System.out.println("In deleteNavigationDetails Method");
		try {
			navigationDetailsManager.deleteNavigationDetails(getNavigationIds());
			obj.put("success", true);
		}     
		catch(Exception e){
			System.out.println("Error in deleteNavigationDetails : "+e.getMessage());
			e.printStackTrace();
		}
		return "success";
	}
	
	public String runNavigationDetails() throws Exception {
		System.out.println("In runNavigationDetails Method");
		try {
			navigationDetailsManager.runNavigationDetails(getNavigationIds());
			obj.put("success", true);
		}     
		catch(Exception e){
			System.out.println("Error in runNavigationDetails : "+e.getMessage());
			e.printStackTrace();
		}
		return "success";
	}
	
	private void setTotalCount() {
		try {		
			totalCount = navigationDetailsManager.getTotalEntires()+"";
		}catch(Exception e) {
			System.out.println("Error in setTotalCount : "+e.getMessage() + "\n" + e);
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
	
}
