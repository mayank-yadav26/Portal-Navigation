package com.spring.manager.impl;

import java.util.ArrayList;

import com.spring.dao.FilmDao;
import com.spring.manager.FilmManager;
import com.spring.model.Film;

public class FilmManagerImpl implements FilmManager{
	FilmDao filmDao;

	@Override
	public ArrayList<Film> getFilmList(String limit, String start) {
		ArrayList<Film> filmList = filmDao.getFilmList(limit,start);
		return filmList;
	}

	@Override
	public int getTotalEntires() {
		return filmDao.getTotalEntires();
	}

	@Override
	public void saveFilmData(Film film) {
		 filmDao.saveFilmData(film);
	}
	
	@Override
	public void updateFilmData(Film film) {
		filmDao.updateFilmData(film);
	}
	
	@Override
	public void deleteFilmData(ArrayList<String> filmList) {
		filmDao.deleteFilmData(filmList);
	}
	
	public FilmDao getFilmDao() {
		return filmDao;
	}

	public void setFilmDao(FilmDao filmDao) {
		this.filmDao = filmDao;
	}

}
