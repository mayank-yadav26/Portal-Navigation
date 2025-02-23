package com.spring.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.orm.hibernate5.HibernateTemplate;

import com.spring.agent.NavigationAgent;
import com.spring.dao.NavigationDetailsDao;
import com.spring.model.NavigationDetails;

public class NavigationDetailsDaoImpl implements NavigationDetailsDao{
	public static final Logger LOGGER = LogManager.getLogger(NavigationDetailsDaoImpl.class);
    private HibernateTemplate hibernateTemplate;

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }
    
    public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
    
	public Session getSession() {
		return hibernateTemplate.getSessionFactory().getCurrentSession();
	}
	
    
	@Override
	public List<NavigationDetails> getNavigationDetailsList(String limit, String start) {
		LOGGER.info("Inside getNavigationDetailsList");
		List<NavigationDetails> navigationDetailsList = new ArrayList<NavigationDetails>();
		try {
			navigationDetailsList = getHibernateTemplate().loadAll(NavigationDetails.class);
		}catch(Exception e) {
			LOGGER.error("Error in getNavigationDetailsList : "+e.getMessage());
		}
		return navigationDetailsList;
	}

	@Override
	public int getTotalEntires() {
		LOGGER.info("Inside getTotalEntires");
		List<NavigationDetails> navigationDetailsList = new ArrayList<NavigationDetails>();
		try {
			navigationDetailsList= getHibernateTemplate().loadAll(NavigationDetails.class);
		}catch(Exception e) {
			LOGGER.error("Error in getTotalEntires : "+e.getMessage());
		}
		return navigationDetailsList.size();
	}

	@Override
	public void saveNavigationDetails(String navigationName,String baseUrl, String requestType, String parameters, String requestHeaders) {
		LOGGER.info("Inside saveNavigationDetails");
		try {
			NavigationDetails  navigationDetails = new NavigationDetails();
			navigationDetails.setNavigationName(navigationName);
			navigationDetails.setBaseUrl(baseUrl);
			navigationDetails.setRequestType(requestType);
			navigationDetails.setParameters(parameters);
			navigationDetails.setRequestHeaders(requestHeaders);
			getHibernateTemplate().save(navigationDetails);
		}catch(Exception e) {
			LOGGER.error("Error in saveNavigationDetails : "+e.getMessage());
		}
	}
	
	@Override
	public void updateNavigationDetails(int navigationId, String navigationName,String baseUrl, String requestType, String parameters,
			String requestHeaders) {
		LOGGER.info("Inside updateNavigationDetails");
		Session session = null;
		try {
			NavigationDetails navigationDetails = (NavigationDetails)getHibernateTemplate().get(NavigationDetails.class, navigationId); 
			navigationDetails.setNavigationName(navigationName);
			navigationDetails.setBaseUrl(baseUrl);
			navigationDetails.setRequestType(requestType);
			navigationDetails.setParameters(parameters);
			navigationDetails.setRequestHeaders(requestHeaders);
			getHibernateTemplate().update(navigationDetails);
		}catch(Exception e) {
			LOGGER.error("Error in updateNavigationDetails : "+e.getMessage());
		}finally {
			if(session!=null) {
				session.close();
			}
		}
	}
	
	@Override
	public void deleteNavigationDetails(String navigationIds) {
		LOGGER.info("In deleteNavigationDetails Method");
		String hqlSelectQuery = "DELETE FROM NavigationDetails nd WHERE nd.navigationId IN :idList";
		ArrayList<Integer> navigationIdsListInt = new ArrayList<>();
		try {
			String[] navigationIdsArr = navigationIds.split(",");
			for(String str : navigationIdsArr) {
				navigationIdsListInt.add(Integer.parseInt(str));
			}
			Query query = getSession().createQuery(hqlSelectQuery);
			query.setParameterList("idList", navigationIdsListInt);
			query.executeUpdate();
		}     
		catch(Exception e){
			LOGGER.error("Error in deleteNavigationDetails : "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Override
	public String runNavigationDetails(String navigationIds) {
		LOGGER.info(navigationIds);
		LOGGER.info("In runNavigationDetails Method");
		String docLink="";
		String hqlSelectQuery = "FROM NavigationDetails nd WHERE nd.navigationId IN :idList";
		ArrayList<Integer> navigationIdsListInt = new ArrayList<>();
		ArrayList<NavigationDetails> navigationDetailsList = new ArrayList<NavigationDetails>();
		try {
			String[] navigationIdsArr = navigationIds.split(",");
			for(String str : navigationIdsArr) {
				navigationIdsListInt.add(Integer.parseInt(str));
			}
			Query<NavigationDetails> query = getSession().createQuery(hqlSelectQuery);
			query.setParameterList("idList", navigationIdsListInt);
			navigationDetailsList=(ArrayList<NavigationDetails>) query.list();
			NavigationAgent navigationAgent = new NavigationAgent();
			docLink = navigationAgent.doNavigation(navigationDetailsList);
			if(!docLink.isEmpty()) {
				LOGGER.info("Navigation is success");
			}
		}     
		catch(Exception e){
			LOGGER.error("Error in runNavigationDetails : "+e.getMessage());
			e.printStackTrace();
		}
		return docLink;
	}
	
	@Override
	public String createNavigationFile(String navigationIds) {
		LOGGER.info(navigationIds);
		LOGGER.info("In createNavigationFile Method");
		String docLink="";
		String hqlSelectQuery = "FROM NavigationDetails nd WHERE nd.navigationId IN :idList";
		ArrayList<Integer> navigationIdsListInt = new ArrayList<>();
		ArrayList<NavigationDetails> navigationDetailsList = new ArrayList<NavigationDetails>();
		try {
			String[] navigationIdsArr = navigationIds.split(",");
			for(String str : navigationIdsArr) {
				navigationIdsListInt.add(Integer.parseInt(str));
			}
			Query query = getSession().createQuery(hqlSelectQuery);
			query.setParameterList("idList", navigationIdsListInt);
			navigationDetailsList=(ArrayList<NavigationDetails>) query.list();
			NavigationAgent navigationAgent = new NavigationAgent();
			docLink = navigationAgent.createNavigationFile(navigationDetailsList);
			if(!docLink.isEmpty()) {
				LOGGER.info("Navigation is success");
			}
		}     
		catch(Exception e){
			LOGGER.error("Error in createNavigationFile : "+e.getMessage());
			e.printStackTrace();
		}
		return docLink;
	}
}
