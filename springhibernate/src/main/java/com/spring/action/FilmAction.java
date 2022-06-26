package com.spring.action;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.spring.manager.FilmManager;
import com.spring.model.Film;

public class FilmAction {
	public static FilmManager filmManager;
	public String totalCount;
	public String limit;
	public String start;
	public String data;
	public String total;
	private Map<String , Object> obj = new HashMap<String, Object>();
	public String title;
	public String specialFeatures;
	public String rating;
	public String language;
	public String director;
	public String description;
	public String releaseYear;
	private String filmId;
	
	public String getFilmData() {
		System.out.println("Inside getFilmData");
		Map<String , Object> temp = new HashMap<String, Object>();
		try {
			ApplicationContext context = new ClassPathXmlApplicationContext("spring/spring-config.xml");
			filmManager=(FilmManager) context.getBean("filmManager");
			setTotalCount();
			ArrayList<Film> movieList = new ArrayList<Film>();
			movieList=filmManager.getFilmList(getLimit(),getStart());
			temp.put("data", movieList);
			temp.put("total", totalCount);
			obj = temp;
			System.out.println("data is "+temp.get("data"));
		}     
		catch(Exception e){
			System.out.println("Error in getFilmData : "+e.getMessage());
			e.printStackTrace();
		}
		return "success";
	}
	
	public String addFilmData() throws Exception {
		System.out.println("In addFilmData Method");
		try {
			Film film = new Film();
			film.setTitle(getTitle());
			film.setSpecialFeatures(getSpecialFeatures().replace(",\\s", ","));
			film.setRating(getRating());
			film.setLanguage(Integer.parseInt(getLanguage()));
			film.setDescription(getDescription());
			film.setReleaseYear(getReleaseYear());
			Date date = new Date();
			film.setLastUpdate(new Timestamp(date.getTime()));
			filmManager.saveFilmData(film);
		}     
		catch(Exception e){
			System.out.println("Error in addFilmData : "+e.getMessage());
			e.printStackTrace();
		}
		return "success";
	}
	
	public String editFilmData() throws Exception {
		System.out.println("In editFilmData Method");
		try {
			Film film = new Film();
			film.setFilmId(Integer.parseInt(getFilmId()));
			film.setTitle(getTitle());
			film.setSpecialFeatures(getSpecialFeatures().replace(",\\s", ","));
			film.setRating(getRating());
			film.setLanguage(Integer.parseInt(getLanguage()));
			film.setDescription(getDescription());
			film.setReleaseYear(getReleaseYear());
			Date date = new Date();
			film.setLastUpdate(new Timestamp(date.getTime()));
			filmManager.updateFilmData(film);
		}     
		catch(Exception e){
			System.out.println("Error in editFilmData : "+e.getMessage());
			e.printStackTrace();
		}
		return "success";
	}
	
	public String deleteFilmData() throws Exception {
		System.out.println("In deleteFilmData Method");
		try {
			String[] filmIdsArr = filmId.split(",");
			ArrayList<String> filmList = new ArrayList<>(Arrays.asList(filmIdsArr));
			filmManager.deleteFilmData(filmList);
		}     
		catch(Exception e){
			System.out.println("Error in deleteFilmData : "+e.getMessage());
			e.printStackTrace();
		}
		return "success";
	}
	
	private void setTotalCount() {
		try {		
			totalCount = filmManager.getTotalEntires()+"";
		}catch(Exception e) {
			System.out.println("Error in setTotalCount : "+e.getMessage() + "\n" + e);
		}
	}
	
	public Map<String, Object> getObj() {
		return obj;
	}

	public void setObj(Map<String, Object> obj) {
		this.obj = obj;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
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

	public FilmManager getFilmManager() {
		return filmManager;
	}

	public void setFilmManager(FilmManager filmManager) {
		this.filmManager = filmManager;
	}

	public String getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSpecialFeatures() {
		return specialFeatures;
	}

	public void setSpecialFeatures(String specialFeatures) {
		this.specialFeatures = specialFeatures;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(String releaseYear) {
		this.releaseYear = releaseYear;
	}

	public String getFilmId() {
		return filmId;
	}

	public void setFilmId(String filmId) {
		this.filmId = filmId;
	}
	
}