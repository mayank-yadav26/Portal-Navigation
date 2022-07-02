package com.spring.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.orm.hibernate5.HibernateTemplate;

import com.spring.agent.NavigationAgent;
import com.spring.dao.NavigationDetailsDao;
import com.spring.model.NavigationDetails;

public class NavigationDetailsDaoImpl implements NavigationDetailsDao{
	HibernateTemplate hibernateTemplate;
	public Session getSession(){
		return hibernateTemplate.getSessionFactory().openSession();
	}
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	@Override
	public ArrayList<NavigationDetails> getNavigationDetailsList(String limit, String start) {
		System.out.println("Inside getNavigationDetailsList");
		Session session = null;
		String hqlSelectQuery = "FROM NavigationDetails";
		ArrayList<NavigationDetails> navigationDetailsList = new ArrayList<NavigationDetails>();
		try {
			session = getSession();
			Query query = session.createQuery(hqlSelectQuery);
			query.setFirstResult(Integer.parseInt(start));
			query.setMaxResults(Integer.parseInt(limit));
			navigationDetailsList=(ArrayList<NavigationDetails>) query.list();
			navigationDetailsList=(ArrayList<NavigationDetails>)getHibernateTemplate().loadAll(NavigationDetails.class);
		}catch(Exception e) {
			System.out.println("Error in getNavigationDetailsList : "+e.getMessage());
		}finally {
			if(session!=null) {
				session.close();
			}
		}
		return navigationDetailsList;
	}

	@Override
	public int getTotalEntires() {
		System.out.println("Inside getTotalEntires");
		//		Session session = null;
		//		String hqlSelectQuery = "FROM Film";
		ArrayList<NavigationDetails> navigationDetailsList = new ArrayList<NavigationDetails>();
		try {
			//			session = getSession();
			//			Query query = session.createQuery(hqlSelectQuery);
			//			filmList=(ArrayList<Film>) query.list();
			navigationDetailsList=(ArrayList<NavigationDetails>)getHibernateTemplate().loadAll(NavigationDetails.class);
		}catch(Exception e) {
			System.out.println("Error in getTotalEntires : "+e.getMessage());
		}
		return navigationDetailsList.size();
	}

	@Override
	public void saveNavigationDetails(String baseUrl, String requestType, String parameters, String requestHeaders) {
		System.out.println("Inside saveNavigationDetails");
		try {
			NavigationDetails  navigationDetails = new NavigationDetails();
			navigationDetails.setBaseUrl(baseUrl);
			navigationDetails.setRequestType(requestType);
			navigationDetails.setParameters(parameters);
			navigationDetails.setRequestHeaders(requestHeaders);
			getHibernateTemplate().save(navigationDetails);
		}catch(Exception e) {
			System.out.println("Error in saveNavigationDetails : "+e.getMessage());
		}
	}
	
	@Override
	public void updateNavigationDetails(int navigationId, String baseUrl, String requestType, String parameters,
			String requestHeaders) {
		System.out.println("Inside updateNavigationDetails");
		Session session = null;
		try {
			session = getSession();
			Transaction txn = session.beginTransaction();
			NavigationDetails navigationDetails = (NavigationDetails)session.get(NavigationDetails.class, navigationId); 
			navigationDetails.setBaseUrl(baseUrl);
			navigationDetails.setRequestType(requestType);
			navigationDetails.setParameters(parameters);
			navigationDetails.setRequestHeaders(requestHeaders);
			getHibernateTemplate().update(navigationDetails);
			txn.commit();
		}catch(Exception e) {
			System.out.println("Error in updateNavigationDetails : "+e.getMessage());
		}finally {
			if(session!=null) {
				session.close();
			}
		}
	}
	
	@Override
	public void deleteNavigationDetails(String navigationIds) {
		System.out.println("In deleteNavigationDetails Method");
		Session session = null;
		String hqlSelectQuery = "DELETE FROM NavigationDetails nd WHERE nd.navigationId IN :idList";
		ArrayList<Integer> navigationIdsListInt = new ArrayList<>();
		try {
			String[] navigationIdsArr = navigationIds.split(",");
			for(String str : navigationIdsArr) {
				navigationIdsListInt.add(Integer.parseInt(str));
			}
			session = getSession();
			Transaction txn = session.beginTransaction();
			Query query = session.createQuery(hqlSelectQuery);
			query.setParameterList("idList", navigationIdsListInt);
			query.executeUpdate();
			txn.commit();
		}     
		catch(Exception e){
			System.out.println("Error in deleteNavigationDetails : "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Override
	public String runNavigationDetails(String navigationIds) {
		System.out.println(navigationIds);
		System.out.println("In runNavigationDetails Method");
		String docLink="";
		Session session = null;
		String hqlSelectQuery = "FROM NavigationDetails nd WHERE nd.navigationId IN :idList";
		ArrayList<Integer> navigationIdsListInt = new ArrayList<>();
		ArrayList<NavigationDetails> navigationDetailsList = new ArrayList<NavigationDetails>();
		try {
			String[] navigationIdsArr = navigationIds.split(",");
			for(String str : navigationIdsArr) {
				navigationIdsListInt.add(Integer.parseInt(str));
			}
			session = getSession();
			Query query = session.createQuery(hqlSelectQuery);
			query.setParameterList("idList", navigationIdsListInt);
			navigationDetailsList=(ArrayList<NavigationDetails>) query.list();
			NavigationAgent navigationAgent = new NavigationAgent();
			docLink = navigationAgent.doNavigation(navigationDetailsList);
			if(!docLink.isEmpty()) {
				System.out.println("Navigation is success");
			}
		}     
		catch(Exception e){
			System.out.println("Error in runNavigationDetails : "+e.getMessage());
			e.printStackTrace();
		}
		return docLink;
	}

}
