package com.spring.manager;

import java.util.ArrayList;

import com.spring.model.Film;

public interface FilmManager {

	ArrayList<Film> getFilmList(String limit, String start);
	int getTotalEntires();
	void saveFilmData(Film film);
	void updateFilmData(Film film);
	void deleteFilmData(ArrayList<String> filmList);
}
