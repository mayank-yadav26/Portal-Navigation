package com.spring.dao.impl;

import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.orm.hibernate5.HibernateTemplate;

import com.spring.dao.NavigationDetailsDao;
import com.spring.model.NavigationDetails;

public class NavigationDetailsDaoImpl implements NavigationDetailsDao{
	HibernateTemplate hibernateTemplate;
	public Session getSession(){
		return hibernateTemplate.getSessionFactory().openSession();
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
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

}