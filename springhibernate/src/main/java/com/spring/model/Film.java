package com.spring.model;

import java.sql.Time;
import java.sql.Timestamp;

public class Film {
	private int filmId;
	private String title;
	private String description;
	private String releaseYear;
	private int language;
	private int originalLanguageId;
	private int rentalDuration;
	private float rentalRate;
	private int length;
	private String rating;
	private String specialFeatures;
	private Timestamp lastUpdate;
//	private String director;

	public int getFilmId() {
		return filmId;
	}
	public void setFilmId(int filmId) {
		this.filmId = filmId;
	}
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
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
		if(releaseYear!=null) {
			String[] releaseYearSplit = releaseYear.split("-");
			this.releaseYear = releaseYearSplit[0];
		}
	}
	public int getLanguage() {
		return language;
	}
	public void setLanguage(int language) {
		this.language = language;
	}
	public int getOriginalLanguageId() {
		return originalLanguageId;
	}
	public void setOriginalLanguageId(int originalLanguageId) {
		this.originalLanguageId = originalLanguageId;
	}
	public int getRentalDuration() {
		return rentalDuration;
	}
	public void setRentalDuration(int rentalDuration) {
		this.rentalDuration = rentalDuration;
	}
	public float getRentalRate() {
		return rentalRate;
	}
	public void setRentalRate(float rentalRate) {
		this.rentalRate = rentalRate;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getSpecialFeatures() {
		return specialFeatures;
	}
	public void setSpecialFeatures(String specialFeatures) {
		this.specialFeatures = specialFeatures;
	}
	public Timestamp getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
//	public String getDirector() {
//		return director;
//	}
//	public void setDirector(String director) {
//		this.director = director;
//	}
}
