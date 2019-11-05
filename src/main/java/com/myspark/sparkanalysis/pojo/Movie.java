package com.myspark.sparkanalysis.pojo;
/**
 * @date: 2019/06/26
 * @author: 范林茂
 */
public class Movie {
	
	/**
	 * 电影Id
	 */
	private String movieId;
	/**
	 * 电影标题
	 */
	private String title;
	/**
	 * 电影类别
	 */
	private String genres;
	
	public Movie() {
		
	}
	public Movie(String movieId, String title, String genres) {
		super();
		this.movieId = movieId;
		this.title = title;
		this.genres = genres;
	}
	public String getMovieId() {
		return movieId;
	}
	public void setMovieId(String movieId) {
		this.movieId = movieId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getGenres() {
		return genres;
	}
	public void setGenres(String genres) {
		this.genres = genres;
	}
	@Override
	public String toString() {
		return "Movie [movieId=" + movieId + ", title=" + title + ", genres=" + genres + "]";
	}
	
	

}
