package com.spring.dao;

import java.util.ArrayList;

import com.spring.model.Film;

public interface FilmDao {

	ArrayList<Film> getFilmList(String limit, String start);

	int getTotalEntires();

	void saveFilmData(Film film);

	void updateFilmData(Film film);

	void deleteFilmData(ArrayList<String> filmList);

}
