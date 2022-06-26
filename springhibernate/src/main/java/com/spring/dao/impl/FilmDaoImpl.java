package com.spring.dao.impl;

import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.orm.hibernate5.HibernateTemplate;

import com.spring.dao.FilmDao;
import com.spring.model.Film;

public class FilmDaoImpl implements FilmDao{
	HibernateTemplate hibernateTemplate;
	public Session getSession(){
		return hibernateTemplate.getSessionFactory().openSession();
	}
	
	@Override
	public ArrayList<Film> getFilmList(String limit, String start) {
		System.out.println("Inside getFilmList");
		Session session = null;
		String hqlSelectQuery = "FROM Film";
		ArrayList<Film> filmList = new ArrayList<Film>();
		try {
			session = getSession();
			Query query = session.createQuery(hqlSelectQuery);
			query.setFirstResult(Integer.parseInt(start));
			query.setMaxResults(Integer.parseInt(limit));
			filmList=(ArrayList<Film>) query.list();
		}catch(Exception e) {
			System.out.println("Error in getFilmList : "+e.getMessage());
		}finally {
			if(session!=null) {
				session.close();
			}
		}
		return filmList;
	}
	
	@Override
	public int getTotalEntires() {
		System.out.println("Inside getTotalEntires");
//		Session session = null;
//		String hqlSelectQuery = "FROM Film";
		ArrayList<Film> filmList = new ArrayList<Film>();
		try {
//			session = getSession();
//			Query query = session.createQuery(hqlSelectQuery);
//			filmList=(ArrayList<Film>) query.list();
			filmList=(ArrayList<Film>)getHibernateTemplate().loadAll(Film.class);
		}catch(Exception e) {
			System.out.println("Error in getTotalEntires : "+e.getMessage());
		}
		return filmList.size();
	}
	
	@Override
	public void saveFilmData(Film film) {
		System.out.println("Inside saveFilmData");
		try {
			getHibernateTemplate().save(film);
		}catch(Exception e) {
			System.out.println("Error in saveFilmData : "+e.getMessage());
		}
	}
	
	@Override
	public void updateFilmData(Film film) {
		System.out.println("Inside updateFilmData");
		Session session = null;
		try {
			session = getSession();
			Transaction txn = session.beginTransaction();
			Film newFilm = (Film)session.get(Film.class, film.getFilmId()); 
			newFilm.setTitle(film.getTitle());
			newFilm.setReleaseYear(film.getReleaseYear());
			newFilm.setSpecialFeatures(film.getSpecialFeatures());
			newFilm.setRating(film.getRating());
			newFilm.setLanguage(film.getLanguage());
			newFilm.setDescription(film.getDescription());
			getHibernateTemplate().update(newFilm);
			txn.commit();
		}catch(Exception e) {
			System.out.println("Error in updateFilmData : "+e.getMessage());
		}finally {
			if(session!=null) {
				session.close();
			}
		}
	}
	
	@Override
	public void deleteFilmData(ArrayList<String> filmList) {
		System.out.println("Inside deleteFilmData");
		Session session = null;
		String hqlSelectQuery = "DELETE FROM Film f WHERE f.filmId IN :idList";
		ArrayList<Integer> filmListInt = new ArrayList<>();
		try {
			for(String str : filmList) {
				filmListInt.add(Integer.parseInt(str));
			}
			session = getSession();
			Transaction txn = session.beginTransaction();
			Query query = session.createQuery(hqlSelectQuery);
			query.setParameterList("idList", filmListInt);
			query.executeUpdate();
			txn.commit();
		}catch(Exception e) {
			System.out.println("Error in deleteFilmData : "+e.getMessage());
		}finally {
			if(session!=null) {
				session.close();
			}
		}
	}
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
}
