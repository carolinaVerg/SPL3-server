package bgu.spl181.net.impl;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.google.gson.annotations.SerializedName;

import bgu.spl181.net.srv.bidi.ConnectionHandler;

public class MovieDataBase implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static ConcurrentHashMap<String, Movie> MovieMap;
	private static int highestId=0;
	private static ReentrantReadWriteLock lock;
	
	@SerializedName("movies")
	private ArrayList<Movie> movies;
	
	
	public MovieDataBase() {
		this.movies=new ArrayList<>();
		MovieMap= new ConcurrentHashMap<>();
		lock = new ReentrantReadWriteLock();
	}
	
	public void updateData() {
		if(movies!=null)
			for(int i=0; i<movies.size(); i++)
				addMovie(movies.get(i));
		for (ConcurrentHashMap.Entry<String, Movie> entry : MovieMap.entrySet()) {
			if(!movies.contains(entry.getValue()))
				movies.add(entry.getValue());
		}
	}
	
	public ArrayList<Movie> getAllmovies1() {
		return movies;
	}
	
	public Movie getMovie(String name) {
		return MovieMap.get(name);
	}
	
	public List<String> getMovieList(){
		List<String> MovieList= new LinkedList<String>();
		for (ConcurrentHashMap.Entry<String, Movie> entry : MovieMap.entrySet()) {
			MovieList.add(entry.getKey());
		}
		return MovieList;
		
	}
	
	public synchronized void  addMovie(Movie newMovie) {
		if(!MovieMap.containsKey(newMovie.getName())) {
			MovieMap.put(newMovie.getName(), newMovie);
			if(!movies.contains(newMovie))
				movies.add(newMovie);
		}
		
		if(newMovie.getId()==highestId)
			highestId=newMovie.getId()+1;
		
	}
	
	public synchronized void removeMovie(Movie movie) {
		MovieMap.remove(movie);
		movies.remove(movie);
		if(movie.getId()==highestId-1)
			highestId--;
		movies.remove(movie);
		
	}
	
	public int getHighestId() {
		return highestId;
	}
	
	public ArrayList<String> getMovieNameList(){
		ArrayList<String> listToReturn = new ArrayList<String>();
		String movieName;
		for (Movie movie : movies) {
			movieName='"'+movie.getName()+'"';
			listToReturn.add(movieName);
		}
		return listToReturn;
	}
	
	public ReentrantReadWriteLock getLock() {
		return this.lock;
	}
	
}
